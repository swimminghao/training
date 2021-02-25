package training.week13.XH.nodes;

import lombok.Getter;
import lombok.ToString;
import training.week13.XH.CompositeNode;
import training.week13.XH.JsonMetaNode;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
public class ArrayNode extends AbstractNode implements CompositeNode {
	protected int maxSize;
	@ToString.Exclude
	@Getter
	private JsonMetaNode child;

	public ArrayNode(String name, int maxSize) {
		super(name, "ARRAY");
		this.maxSize = maxSize;
	}

	@Override
	public void addChild(JsonMetaNode child) {
		this.child = merge(this.child, child);
		if (this.child != null) {
			this.child.setParent(this);
		}
	}

	@Override
	public List<JsonMetaNode> getChildren() {
		return child == null ? Collections.emptyList() : Collections.singletonList(child);
	}

	@Override
	public String getType() {
		return child == null ? "EMPTY-ARRAY" : String.format("ARRAY[%s]", child.getType());
	}
}