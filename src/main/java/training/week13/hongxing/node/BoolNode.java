package training.week13.hongxing.node;

import lombok.Getter;

public class BoolNode extends AbstractNode {
	@Getter private NodeType type = NodeType.bool;
	@Getter private boolean isLeaf = true;

	public BoolNode(String name) {
		super(name);
	}

	@Override
	protected Node doMerge(Node other) {
		typeCheck(other, "merge");
		return this;
	}
}