package training.week13.fz.nodes;

import lombok.Getter;
import lombok.ToString;
import training.week13.fz.PrimitiveNode;

@ToString(callSuper = true)
@Getter
public class TextNode extends AbstractNode implements PrimitiveNode {
	protected int minLength, maxLength;

	public TextNode(String name, int minLength, int maxLength) {
		super(name, "TEXT");
		this.minLength = minLength;
		this.maxLength = maxLength;
	}
}