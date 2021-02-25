package training.week13.hongxing.node;

import lombok.Getter;

public class NumberNode extends AbstractNode {
	@Getter private NodeType type = NodeType.number;
	@Getter private boolean isLeaf = true;

	public NumberNode(String name) {
		super(name);
	}

	@Override
	protected Node doMerge(Node other) {
		if (NodeType.integer.equals(other.getType())) {
			return this;
		}
		typeCheck(other, "merge");
		return this;
	}
}