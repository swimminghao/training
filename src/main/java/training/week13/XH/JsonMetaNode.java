package training.week13.XH;

import java.util.List;

public interface JsonMetaNode {
	String getName();

	JsonMetaNode getParent();

	void setParent(JsonMetaNode parent);

	List<String> getPath();

	String getType();

	JsonMetaNode merge(JsonMetaNode node);

	String prettyPrint();
}
