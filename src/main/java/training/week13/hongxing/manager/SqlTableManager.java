package training.week13.hongxing.manager;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import training.week13.hongxing.node.Node;
import training.week13.hongxing.node.Nodes;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class SqlTableManager implements TableManager<SqlTable> {
	private final Connection connection;
	private final ManageParameter parameter;
	private boolean ensured = false;
	@Getter private SqlTable table;

	@Override
	public void close() throws Exception {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	@Override
	public void initTable(Iterable<JSON> jsons) {
		ensured = false;
		Node node = Nodes.parseNode(parameter.getName(), parameter.getArrayElementName(), jsons);
		SqlTable table = SqlTable.builder().name(node.getName()).build();
		Deque<Pair<SqlTable, Node>> stack = new LinkedList<>();
		for (Pair<SqlTable, Node> pair = Pair.of(table, node); pair != null; pair = stack.poll()) {
			Node cNode = pair.getRight();
			SqlTable cTable = pair.getLeft();
			Node.NodeType type = cNode.getType();
			if (Node.NodeType.object.equals(type) || Node.NodeType.array.equals(type)) {
				cTable.addColumn(SqlColumn.builder()
						.name(parameter.getPkName())
						.type(SqlColumn.SqlColumnType.VARCHAR)
						.size(40)
						.isNullable(false)
						.build());
				cTable.addIndex(null, SqlTable.SqlIndexType.PRIMARY, parameter.getPkName());
			}
			if (cTable.getParentTable() != null) {
				cTable.addColumn(SqlColumn.builder()
						.name(parameter.getParentPkName())
						.type(SqlColumn.SqlColumnType.VARCHAR)
						.size(40)
						.isNullable(false)
						.build());
			}
			if (type.equals(Node.NodeType.array)) {
				cTable.addColumn(SqlColumn.builder()
						.name(parameter.getArrayIndexName())
						.type(SqlColumn.SqlColumnType.TINYINT)
						.size(4)
						.isNullable(false)
						.build());
			}
			for (Node child : cNode.getChildren()) {
				Node.NodeType childType = child.getType();
				if (Node.NodeType.object.equals(childType) || Node.NodeType.array.equals(childType)) {
					SqlTable subTable = SqlTable.builder()
							.name(parameter.getTableNameGenerator().apply(cTable.getName(), child.getName()))
							.parentTable(cTable)
							.build();
					cTable.addSubTable(subTable);
					stack.push(Pair.of(subTable, child));
				} else {
					SqlColumn column = SqlColumn.fromNode(child);
					//todo json cloumn name cant equals to custom column
					if (column != null) {
						cTable.addColumn(column);
					}
				}
			}
		}
		this.table = table;
	}

	@Override
	public void writeTable() throws ManageException {
		List<SqlTable> missTables = detectMissTable(connection, table.allTables());
		if (!missTables.isEmpty()) {
			try {
				boolean autoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
				for (SqlTable table : missTables) {
					Statement statement = connection.createStatement();
					int result = statement.executeUpdate(ManageUtils.createSql(table));
					if (result != 0) {
						throw new RuntimeException(String.format("create table fail, table = %s", table));
					}
				}
				connection.commit();
				connection.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		if (!detectMissTable(connection, table.allTables()).isEmpty()) {
			throw new ManageException("still exist miss table ");
		}
	}

	@Override
	public void writeRecord(JSON record) throws ManageException {
		writeRecords(Collections.singleton(record));
	}

	@Override
	public void writeRecords(Iterable<JSON> records) throws ManageException {
		ensureDbTableExist();
		List<String> insertSqls = StreamSupport.stream(records.spliterator(), false)
				.flatMap(r -> ManageUtils.insertSql(table, r, parameter).stream())
				.collect(Collectors.toList());
		try {
			Statement statement = connection.createStatement();
			for (String sql : insertSqls) {
				boolean execute = statement.execute(sql);
			}
		} catch (SQLException e) {
			throw new ManageException(e);
		}
	}

	public void writeRecordTx(JSON record) throws ManageException {
		writeRecordsTx(Collections.singleton(record));
	}

	public void writeRecordsTx(Iterable<JSON> records) throws ManageException {
		try {
			boolean autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			writeRecords(records);
			connection.commit();
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new ManageException(e);
		}
	}

	private void ensureDbTableExist() throws ManageException {
		if (!ensured) {
			writeTable();
			ensured = true;
		}
	}

	private static List<SqlTable> detectMissTable(Connection connection, Map<String, SqlTable> allTables) throws ManageException {
		try {
			List<SqlTable> existTables = new ArrayList<>();
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet dbTables = metaData.getTables(null, null, null, null);
			while (dbTables.next()) {
				String tableName = dbTables.getString(3);
				SqlTable virtualTable = allTables.get(tableName);
				if (virtualTable != null) {
					existTables.add(virtualTable);
					// todo-hx: 2020/1/2 table match
				}
			}
			return allTables.values().stream().filter(table -> !existTables.contains(table)).collect(Collectors.toList());
		} catch (SQLException e) {
			throw new ManageException(e);
		}
	}
}
