package training.week13.gujc.node;

import training.week13.gujc.AbstractNode;
import training.week13.gujc.NodeType;
import training.week13.gujc.SimpleNode;


public class BooleanNode extends AbstractNode implements SimpleNode {

    public BooleanNode(String name, Boolean value) {
        super(name, NodeType.BOOLEAN);
        this.value = value;
    }

    private Boolean value;

    @Override
    public Boolean getValue() {
        return value;
    }
}
