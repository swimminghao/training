package training.week13.hongxing.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import training.week13.hongxing.node.*;

@AllArgsConstructor
@Builder
@Getter
public class SqlColumn implements Column {
	private String name;
	private SqlColumnType type;
	private Integer size;
	@Builder.Default private final boolean isNullable = true;

	public static SqlColumn fromNode(Node node) {
		SqlColumnBuilder builder = SqlColumn.builder().name(node.getName());
		if (node instanceof BoolNode) {
			builder.type(SqlColumnType.BIT);
		} else if (node instanceof StringNode) {
			builder.type(SqlColumnType.VARCHAR).size(((StringNode) node).getSize());
		} else if (node instanceof NumberNode) {
			builder.type(SqlColumnType.DOUBLE);
		} else if (node instanceof IntegerNode) {
			builder.type(SqlColumnType.BIGINT);
		} else if (node instanceof EmptyNode) {
			return null;
		} else {
			throw new RuntimeException("");
		}
		return builder.build();
	}

	@AllArgsConstructor
	@Getter
	public enum SqlColumnType {
		BIT("bit"),
		TINYINT("tinyint"),
		INT("int"),
		BIGINT("bigint"),
		DOUBLE("double"),
		VARCHAR("varchar"),
		DATETIME("datetime"),
		TEXT("text");

		private String typeName;
	}
}
