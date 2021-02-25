package training.week13.hongxing.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManageUtils {

	public static String createSql(SqlTable table) {
		StringBuilder sql = new StringBuilder("CREATE TABLE `" + table.getName() + "` (");
		sql.append(table.getColumns().stream().map(column -> "`" + column.getName() + "` " +
				column.getType().getTypeName() + " " +
				(column.getSize() == null ? "" : "(" + column.getSize() + ")") + " " +
				(column.isNullable() ? "DEFAULT NULL" : "NOT NULL")).collect(Collectors.joining(",")));
		List<SqlTable.SqlIndex> indexs = table.getIndexs();
		if (!indexs.isEmpty()) {
			sql.append(indexs.stream().map(index -> "," + index.getType().getTypeName() +
					ObjectUtils.firstNonNull(index.getIndexName(), "") +
					"(" + String.join(",", index.getColumns().stream().map(c -> "`" + c.getName() + "`").toArray(String[]::new)) + ")")
					.collect(Collectors.joining("")));
		}
		return sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 ;").toString();
	}

	public static List<String> createSqlRecursive(SqlTable table) {
		return table.allTables().values().stream().map(ManageUtils::createSql).collect(Collectors.toList());
	}

	public static String toSqlStr(Object object) {
		if (object == null) {
			return "null";
		} else if (object instanceof CharSequence) {
			return "'" + object.toString() + "'";
		} else if (object instanceof Date) {
			return "'" + DateFormat.getDateTimeInstance().format(object) + "'";
		} else if (object instanceof Number || object instanceof Boolean) {
			return object.toString();
		} else {
			throw new RuntimeException("unsupported type " + object.getClass());
		}
	}

	public static List<String> insertSql(SqlTable sqlTable, JSON json, ManageParameter parameter) {
		List<String> insertSqls = new ArrayList<>();
		Deque<Triple<SqlTable, Object, Object>> stack = new LinkedList<>();
		for (Triple<SqlTable, Object, Object> triple = Triple.of(sqlTable, json, null); triple != null; triple = stack.poll()) {
			SqlTable table = triple.getLeft();
			Object data = triple.getMiddle(), pId = triple.getRight();
			String sqlPrefix = String.format("INSERT INTO `%s`%s VALUES ",
					table.getName(), table.getColumns().stream().map(a -> "`" + a.getName() + "`").collect(Collectors.joining(",", "(", ")")));
			if (data instanceof Map) {
				Object id = table.generateId();
				List<String> insertValues = new ArrayList<>();
				for (SqlColumn column : table.getColumns()) {
					if (parameter.getPkName().equals(column.getName())) {
						insertValues.add(toSqlStr(id));
					} else if (parameter.getParentPkName().equals(column.getName())) {
						insertValues.add(toSqlStr(pId));
					} else {
						insertValues.add(toSqlStr(((Map) data).get(column.getName())));
					}
				}
				Map<String, SqlTable> subTables = table.getSubTables();
				for (Map.Entry<String, Object> entry : ((JSONObject) data).entrySet()) {
					Object value = entry.getValue();
					SqlTable subTable = subTables.get(parameter.getTableNameGenerator().apply(table.getName(), entry.getKey()));
					if ((value instanceof Map || value instanceof List) && subTable != null) {
						stack.push(Triple.of(subTable, value, id));
					}
				}
				insertSqls.add(sqlPrefix + "(" + String.join(",", insertValues) + ");");
			} else if (data instanceof List) {
				int index = 0;
				for (Object value : ((List) data)) {
					Object id = table.generateId();
					List<String> insertValues = new ArrayList<>();
					for (SqlColumn column : table.getColumns()) {
						if (parameter.getPkName().equals(column.getName())) {
							insertValues.add(toSqlStr(id));
						} else if (parameter.getParentPkName().equals(column.getName())) {
							insertValues.add(toSqlStr(pId));
						} else if (parameter.getArrayIndexName().equals(column.getName())) {
							insertValues.add(toSqlStr(index++));
						} else {
							insertValues.add(toSqlStr(value));
						}
					}
					insertSqls.add(sqlPrefix + "(" + String.join(",", insertValues) + ");");
					if ((value instanceof Map || value instanceof List) && !table.getSubTables().isEmpty()) {
						stack.push(Triple.of(((List<SqlTable>) new ArrayList<>(table.getSubTables().values())).get(0), value, id));
					}
				}
			} else {
				throw new RuntimeException("unsupported data type " + data.getClass().getName());
			}
		}
		return insertSqls;
	}

	public static void main(String[] args) {
		try (InputStream is = Resources.class.getResourceAsStream("/week13/json-20191225.txt");
		     Connection connection = DBUtil.getConnection()) {
			List<JSON> jsons = IOUtils
					.readLines(is, "UTF-8").stream().map(JSON::parseObject)
					.map(JSON.class::cast).collect(Collectors.toList());
			SqlTableManager manager = new SqlTableManager(connection, ManageParameter.builder().name("hx_root").build());
			manager.initTable(jsons);
			manager.writeRecordsTx(jsons);
		} catch (IOException | SQLException | ManageException e) {
			e.printStackTrace();
		}
	}
}
