package training.week13.fz.writer.mysql;

import com.wayue.olympus.common.LambdaUtil;
import com.wayue.olympus.common.sql.SQLBatchExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import training.week13.fz.writer.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class MySQLTable implements Table<MySQLColumn> {
	private final List<MySQLColumn> columns = new ArrayList<>();
	private final Connection connection;
	private SQLBatchExecutor insert;
	@Setter private String name;

	public MySQLTable(Connection connection, String name) {
		this.connection = connection;
		this.name = name;
	}

	@Override
	public void addColumn(MySQLColumn column) {
		this.columns.add(column);
	}

	@Override
	public void close() throws SQLException {
		this.insert.close();
		this.columns.stream()
				.filter(MySQLColumn::isComposite)
				.map(MySQLColumn::getSubTable)
				.forEach(LambdaUtil.wrap(MySQLTable::close));
	}

	@Override
	public Object generateID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	@Override
	public MySQLColumn getColumn(String name) {
		return this.columns.stream().filter(column -> name.equals(column.getName())).findFirst().orElse(null);
	}

	@Override
	public void open() throws SQLException {
		{//create
			String columns = this.columns.stream()
					.filter(MySQLColumn::isPrimitive)
					.map(c -> String.format("`%s` %s", c.getName(), c.getDatatype()))
					.collect(Collectors.joining(","));
			{
				String constraints = this.columns.stream()
						.filter(MySQLColumn::isPrimitive)
						.filter(MySQLColumn::isConstraint)
						.map(MySQLColumn::getName)
						.map(n -> String.format("`%s`", n))
						.collect(Collectors.joining(","));
				columns += constraints.isEmpty() ? "" : ", unique (" + constraints + ")";
			}
			{
				String primaries = this.columns.stream()
						.filter(MySQLColumn::isPrimitive)
						.filter(MySQLColumn::isPrimary)
						.map(MySQLColumn::getName)
						.map(n -> String.format("`%s`", n))
						.collect(Collectors.joining(","));
				columns += primaries.isEmpty() ? "" : ", primary key (" + primaries + ")";
			}
			String sql = String.format("create table if not exists `%s` (%s)", name, columns);
			log.info(sql);
			try (PreparedStatement create = this.connection.prepareStatement(sql)) {
				create.execute();
			}
			this.columns.stream()
					.map(MySQLColumn::getSubTable)
					.filter(Objects::nonNull)
					.forEach(LambdaUtil.wrap(MySQLTable::open));
		}
		{//prepare insert
			String columns = this.columns.stream()
					.filter(MySQLColumn::isPrimitive)
					.map(MySQLColumn::getName)
					.map(n -> String.format("`%s`", n))
					.collect(Collectors.joining(","));
			String values = this.columns.stream()
					.filter(MySQLColumn::isPrimitive)
					.map(c -> "?")
					.collect(Collectors.joining(","));
			insert = new SQLBatchExecutor(connection, String.format("insert into `%s` (%s) values (%s)", name, columns, values));
		}
	}

	@Override
	public boolean removeColumn(String name) {
		return this.columns.removeIf(column -> name.equals(column.getName()));
	}

	@Override
	public void write(Object pid, int index, Object o) throws SQLException {
		List<Object> values = new ArrayList<>();
		Object id = generateID();
		for (MySQLColumn column : this.columns) {
			if (MySQLTableManager.ID.equals(column.getKey())) {
				values.add(id);
			} else if (MySQLTableManager.PID.equals(column.getKey())) {
				values.add(pid);
			} else if (MySQLTableManager.INDEX.equals(column.getKey())) {
				values.add(index);
			} else if (column.isPrimitive()) {
				values.add(column.getValue(o));
			} else if (o instanceof Map) {
				Object v = column.getValue(o);
				if (v instanceof List) {
					for (int i = 0; i < ((List) v).size(); i++) {
						column.getSubTable().write(id, i, ((List) v).get(i));
					}
				} else {
					column.getSubTable().write(id, 0, v);
				}
			} else if (o instanceof List) {
				for (int i = 0; i < ((List) o).size(); i++) {
					column.getSubTable().write(id, i, ((List) o).get(i));
				}
			}
		}
		insert.append(values);
	}

	@Override
	public void write(Object o) throws SQLException {
		log.info("write: " + o);
		write(null, 0, o);
	}
}
