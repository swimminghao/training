package training.week13.fz.nodes;

import lombok.ToString;
import training.week13.fz.PrimitiveNode;

@ToString(callSuper = true)
public class NullNode extends AbstractNode implements PrimitiveNode {
	public NullNode(String name) {
		super(name, "<null>");
	}
}