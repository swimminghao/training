package training.week13.hongxing.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class SqlTable implements Table<SqlColumn, SqlTable> {
	private String name;
	private SqlTable parentTable;
	@Builder.Default private final Map<String, SqlColumn> columns = new LinkedHashMap<>();
	@Builder.Default private final Map<String, SqlTable> subTables = new LinkedHashMap<>();
	@Builder.Default private final List<SqlIndex> indexs = new ArrayList<>();

	public SqlTable addIndex(String indexName, SqlIndexType type, String... columns) {
		if (columns == null || columns.length == 0) {
			return this;
		}
		indexs.add(SqlIndex.builder()
				.type(type)
				.indexName(indexName)
				.columns(Arrays.stream(columns).map(this.columns::get).collect(Collectors.toList()))
				.build());
		return this;
	}

	@Override
	public List<SqlColumn> getColumns() {
		return new ArrayList<>(columns.values());
	}

	@Override
	public SqlTable addColumn(SqlColumn column) {
		columns.put(column.getName(), column);
		return this;
	}

	@Override
	public SqlTable addSubTable(SqlTable table) {
		subTables.put(table.getName(), table);
		return this;
	}

	@Override
	public Map<String, SqlTable> allTables() {
		Map<String, SqlTable> tables = new LinkedHashMap<>();
		Deque<SqlTable> stack = new LinkedList<>();
		for (SqlTable c = this; c != null; c = stack.poll()) {
			tables.put(c.getName(), c);
			stack.addAll(c.getSubTables().values());
		}
		return tables;
	}

	@Override
	public Object generateId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	@Builder
	@AllArgsConstructor
	@Getter
	public static class SqlIndex {
		private String indexName;
		private SqlIndexType type;
		private String comment;
		private List<SqlColumn> columns;
	}

	@AllArgsConstructor
	@Getter
	public enum SqlIndexType {
		PRIMARY("PRIMARY KEY"),
		UNIQUE("UNIQUE KEY"),
		NORMAL("KEY");

		private String typeName;
	}
}
