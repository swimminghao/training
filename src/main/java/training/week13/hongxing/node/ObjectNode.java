package training.week13.hongxing.node;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectNode extends AbstractNode {
	@Getter private NodeType type = NodeType.object;
	@Getter private boolean isLeaf = false;

	public ObjectNode(String name) {
		super(name);
	}

	@Override
	public Node doMerge(Node other) {
		typeCheck(other, "merge");
		Map<String, Node> nodeMap = getChildren().stream().collect(Collectors.toMap(Node::getName, Function.identity()));
		for (Node otherChild : other.getChildren()) {
			Node child = nodeMap.get(otherChild.getName());
			if (child == null) {
				nodeMap.put(otherChild.getName(), otherChild);
			} else {
				nodeMap.put(otherChild.getName(), child.merge(otherChild));
			}
		}
		setChildren(new ArrayList<>(nodeMap.values()));
		return this;
	}
}