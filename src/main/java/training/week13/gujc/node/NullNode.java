package training.week13.gujc.node;

import training.week13.gujc.AbstractNode;
import training.week13.gujc.NodeType;
import training.week13.gujc.SimpleNode;


public class NullNode extends AbstractNode implements SimpleNode {

    public NullNode(String name) {
        super(name, NodeType.EMPTY);
    }

    @Override
    public Object getValue() {
        return null;
    }
}
