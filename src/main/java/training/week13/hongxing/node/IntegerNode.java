package training.week13.hongxing.node;

import lombok.Getter;

public class IntegerNode extends AbstractNode {
	@Getter private NodeType type = NodeType.integer;
	@Getter private boolean isLeaf = true;

	public IntegerNode(String name) {
		super(name);
	}

	@Override
	protected Node doMerge(Node other) {
		if (NodeType.number.equals(other.getType())) {
			return other;
		}
		typeCheck(other, "merge");
		return this;
	}
}