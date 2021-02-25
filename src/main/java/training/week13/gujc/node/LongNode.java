package training.week13.gujc.node;

import training.week13.gujc.AbstractNode;
import training.week13.gujc.NodeType;
import training.week13.gujc.SimpleNode;

public class LongNode extends AbstractNode implements SimpleNode {

    public LongNode(String name, Long value) {
        super(name, NodeType.LONG);
        this.value = value;
    }

    private Long value;

    @Override
    public Long getValue() {
        return value;
    }
}
