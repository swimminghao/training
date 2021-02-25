package training.week13.hongxing.node;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class ArrayNode extends AbstractNode {
	@Getter private NodeType type = NodeType.array;
	@Getter private boolean isLeaf = false;
	@Getter private int size;

	public ArrayNode(String name, String childName, int size) {
		super(name);
		this.size = size;
		addChild(new EmptyNode(childName));
	}

	@Override
	protected Node doMerge(Node other) {
		typeCheck(other, "merge");
		Node otherChild = other.getChildren().get(0);
		setChildren(Collections.singletonList(getChildren().get(0).merge(otherChild)));
		size = Math.max(((ArrayNode) other).size, size);
		return this;
	}

	@Override
	public void addChild(Node child) {
		List<Node> children = getChildren();
		if (CollectionUtils.isEmpty(children)) {
			super.addChild(child);
			return;
		}
		child.setParent(this);
		setChildren(Collections.singletonList(getChildren().get(0).merge(child)));
	}
}