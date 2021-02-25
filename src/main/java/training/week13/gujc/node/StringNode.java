package training.week13.gujc.node;

import training.week13.gujc.AbstractNode;
import training.week13.gujc.NodeType;
import training.week13.gujc.SimpleNode;

public class StringNode extends AbstractNode implements SimpleNode {

    public StringNode(String name, String value) {
        super(name, NodeType.STRING);
        this.value = value;
    }

    private String value;

    @Override
    public String getValue() {
        return value;
    }
}
