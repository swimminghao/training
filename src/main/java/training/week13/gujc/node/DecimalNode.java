package training.week13.gujc.node;

import training.week13.gujc.AbstractNode;
import training.week13.gujc.NodeType;
import training.week13.gujc.SimpleNode;

import java.math.BigDecimal;

public class DecimalNode extends AbstractNode implements SimpleNode {

    public DecimalNode(String name, BigDecimal value) {
        super(name, NodeType.DECIMAL);
        this.value = value;
    }

    private BigDecimal value;

    @Override
    public BigDecimal getValue() {
        return value;
    }
}
