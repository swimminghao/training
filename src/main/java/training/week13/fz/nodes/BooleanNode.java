package training.week13.fz.nodes;

import lombok.ToString;
import training.week13.fz.PrimitiveNode;

@ToString(callSuper = true)
public class BooleanNode extends AbstractNode implements PrimitiveNode {
	public BooleanNode(String name) {
		super(name, "BOOLEAN");
	}
}