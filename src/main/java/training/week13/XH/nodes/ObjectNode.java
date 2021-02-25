package training.week13.XH.nodes;

import lombok.ToString;
import training.week13.XH.CompositeNode;
import training.week13.XH.JsonMetaNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString(callSuper = true)
public class ObjectNode extends AbstractNode implements CompositeNode {
	@ToString.Exclude protected Map<String, JsonMetaNode> children = new HashMap<>();

	public ObjectNode(String name) {
		super(name, "OBJECT");
	}

	@Override
	public void addChild(JsonMetaNode child) {
		children.put(child.getName(), merge(children.get(child.getName()), child));
		child.setParent(this);
	}

	@Override
	public List<JsonMetaNode> getChildren() {
		return new ArrayList<>(children.values());
	}
}