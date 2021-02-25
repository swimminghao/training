package training.week13.hongxing.node;

import lombok.Getter;

public class EmptyNode extends AbstractNode {
	@Getter private NodeType type = NodeType.empty;
	@Getter private boolean isLeaf = true;

	public EmptyNode(String name) {
		super(name);
	}

	@Override
	protected Node doMerge(Node other) {
		return other;
	}
}
