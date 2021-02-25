package training.week13.XH.nodes;

import lombok.ToString;
import training.week13.XH.OriginalNode;

@ToString(callSuper = true)
public class BooleanNode extends AbstractNode implements OriginalNode {
	public BooleanNode(String name) {
		super(name, "BOOLEAN");
	}
}