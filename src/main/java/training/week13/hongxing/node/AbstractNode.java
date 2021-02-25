package training.week13.hongxing.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "parent")
@RequiredArgsConstructor
public abstract class AbstractNode implements Node {
	private final String name;
	private Node parent;
	protected List<Node> children = new ArrayList<>();

	protected abstract boolean isLeaf();

	@Override
	public final Node merge(Node other) {
		if (!NodeType.empty.equals(other.getType())) {
			return doMerge(other);
		}
		return this;
	}

	protected abstract Node doMerge(Node other);

	protected final void typeCheck(Node other, String operateName) {
		if (!getType().equals(other.getType())) {
			throw new UnsupportedOperationException(String.format("[%s] is not supported for NodeType [%s] - [%s]", operateName, getType(), other.getType()));
		}
	}

	@Override
	public void addChild(Node child) {
		if (isLeaf()) {
			throw new UnsupportedOperationException(String.format("unsupported operation for NodeType [%s]", this.getType()));
		}
		if (children.stream().anyMatch(a -> a.getName().equals(child.getName()))) {
			throw new RuntimeException(String.format("duplicate key [%s] in Node [%s]", child.getName(), this.toString()));
		}
		children.add(child);
		if (child.getParent() != null) {
			throw new IllegalArgumentException("node have more than one parent " + child);
		}
		child.setParent(this);
	}
}