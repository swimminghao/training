package training.week13.hongxing.node;

import java.util.List;

public interface Node {

	String getName();

	List<Node> getChildren();

	Node getParent();

	void setParent(Node parent);

	void setChildren(List<Node> children);

	void addChild(Node child);

	NodeType getType();

	Node merge(Node other);

	enum NodeType {
		object, number, string, array, bool, integer, empty,
	}
}