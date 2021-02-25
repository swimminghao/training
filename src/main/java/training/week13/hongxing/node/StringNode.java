package training.week13.hongxing.node;

import lombok.Getter;

public class StringNode extends AbstractNode {
	@Getter private NodeType type = NodeType.string;
	@Getter private boolean isLeaf = true;
	@Getter private int size;

	public StringNode(String name, int size) {
		super(name);
		this.size = size;
	}

	@Override
	protected Node doMerge(Node other) {
		typeCheck(other, "merge");
		size = Math.max(((StringNode) other).size, size);
		return this;
	}
}