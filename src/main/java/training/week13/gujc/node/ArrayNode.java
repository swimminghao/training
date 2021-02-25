package training.week13.gujc.node;

import lombok.Getter;
import lombok.Setter;
import training.week13.gujc.AbstractNode;
import training.week13.gujc.BaseNode;
import training.week13.gujc.ComplexNode;
import training.week13.gujc.NodeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayNode extends AbstractNode implements ComplexNode {

    @Getter
    private int maxSize;

    @Setter
    private List<BaseNode> children = new ArrayList<>();

    public ArrayNode(String name) {
        super(name, NodeType.ARRAY);
    }

    @Override
    public List<BaseNode> getChildren() {
        return children;
    }

    @Override
    public void add(BaseNode node) {
        children.add(node);
        maxSize++;
    }

    @Override
    public ArrayNode merge(ComplexNode otherNode) {
        if (otherNode != null) {
            ArrayNode node = (ArrayNode) otherNode;
            BaseNode otherChild = node.getChildren().get(0);
            if (otherChild instanceof ObjectNode) {
                ObjectNode thisChild = (ObjectNode) this.getChildren().get(0);
                this.children = Collections.singletonList(thisChild.merge((ObjectNode) otherChild));
            }
            if (this.maxSize < node.getMaxSize()) {
                this.maxSize = node.getMaxSize();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        if (children.size() > 0) {
            return String.format("%s {%s[%s-%d]}", getName(), getType(), children.get(0).getType(), maxSize);
        } else {
            return String.format("%s {%s[0]}", getName(), getType());
        }
    }
}
