package training.week13.XH.nodes;

import lombok.ToString;
import training.week13.XH.OriginalNode;

@ToString(callSuper = true)
public class NullNode extends AbstractNode implements OriginalNode {
	public NullNode(String name) {
		super(name, "<null>");
	}
}