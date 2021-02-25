package training.week13.fz;

import java.util.List;

public interface SchemaNode {
	String getName();

	SchemaNode getParent();

	void setParent(SchemaNode parent);

	List<String> getPath();

	String getType();

	SchemaNode merge(SchemaNode node);

	String prettyPrint();
}
