package training.week13.fz.nodes;

import lombok.ToString;
import training.week13.fz.CompositeNode;
import training.week13.fz.SchemaNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString(callSuper = true)
public class ObjectNode extends AbstractNode implements CompositeNode {
	@ToString.Exclude protected Map<String, SchemaNode> children = new HashMap<>();

	public ObjectNode(String name) {
		super(name, "OBJECT");
	}

	@Override
	public void addChild(SchemaNode child) {
		children.put(child.getName(), merge(children.get(child.getName()), child));
		child.setParent(this);
	}

	@Override
	public List<SchemaNode> getChildren() {
		return new ArrayList<>(children.values());
	}
}