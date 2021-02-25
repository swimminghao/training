package training.week13.fz.writer.mysql;

import lombok.RequiredArgsConstructor;
import training.week13.fz.CompositeNode;
import training.week13.fz.SchemaNode;
import training.week13.fz.nodes.*;
import training.week13.fz.nodes.NumberNode.NumberType;
import training.week13.fz.writer.TableManager;

import java.sql.Connection;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MySQLTableManager implements TableManager<MySQLTable> {
	public static final String ID = "ID", PID = "PID", INDEX = "INDEX";
	private static final String ID_TYPE = "char(32)", INDEX_TYPE = "tinyint";
	private final Connection connection;

	private void columns(ObjectNode node, Consumer<MySQLColumn> consumer) {
		for (SchemaNode c : node.getChildren()) {
			if (!(c instanceof NullNode)) {
				MySQLColumn column = new MySQLColumn(c.getName(), getDataType(c), false, false);
				if (c instanceof CompositeNode) {
					column.setSubTable(create((CompositeNode) c));
				}
				consumer.accept(column);
			}
		}
	}

	@Override
	public MySQLTable create(CompositeNode node) {
		MySQLTable table = new MySQLTable(connection, getName(node.getPath()));
		table.addColumn(new MySQLColumn(ID, ID_TYPE, false, true));
		if (node.getParent() != null) {
			table.addColumn(new MySQLColumn(PID, ID_TYPE, true, false));
		}
		if (node instanceof ArrayNode) {
			table.addColumn(new MySQLColumn(INDEX, INDEX_TYPE, true, false));
			SchemaNode child = ((ArrayNode) node).getChild();
			if (!(child == null || child instanceof NullNode)) {
				table.setName(getName(child.getPath()));
				MySQLColumn column = new MySQLColumn(child.getName(), getDataType(child), false, false);
				if (child instanceof ArrayNode) {
					column.setSubTable(create((ArrayNode) child));
					table.addColumn(column);
				} else if (child instanceof ObjectNode) {
					columns((ObjectNode) child, table::addColumn);
				} else {
					table.addColumn(column);
				}
			}
		} else if (node instanceof ObjectNode) {
			columns((ObjectNode) node, table::addColumn);
		}
		return table;
	}

	private String getDataType(SchemaNode child) {
		if (child instanceof CompositeNode) {
			return null;
		} else if (child instanceof BooleanNode) {
			return "bit";
		} else if (child instanceof NullNode) {
			throw new RuntimeException("null column not supported");
		} else if (child instanceof NumberNode) {
			NumberType numberType = ((NumberNode) child).getNumberType();
			switch (numberType) {
				case INTEGER:
					return "integer";
				case LONG:
					return "bigint";
				case DECIMAL:
					return "double";
				default:
					throw new RuntimeException("unknown number type " + numberType);
			}
		} else if (child instanceof TextNode) {
			return String.format("nvarchar(%d)", Integer.highestOneBit(((TextNode) child).getMaxLength()) << 1);
		} else {
			throw new RuntimeException("unknown type " + child.getClass());
		}
	}

	private String getName(List<String> path) {
		return "fz_" + String.join(".", path);
	}
}
