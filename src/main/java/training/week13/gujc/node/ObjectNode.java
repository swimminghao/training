package training.week13.gujc.node;

import lombok.Setter;
import training.week13.gujc.AbstractNode;
import training.week13.gujc.BaseNode;
import training.week13.gujc.ComplexNode;
import training.week13.gujc.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjectNode extends AbstractNode implements ComplexNode {
    public ObjectNode(String name) {
        super(name, NodeType.OBJECT);
    }

    @Setter
    private List<BaseNode> children = new ArrayList<>();

    @Override
    public List<BaseNode> getChildren() {
        return children;
    }

    @Override
    public void add(BaseNode node) {
        children.add(node);
    }

    @Override
    public ObjectNode merge(ComplexNode otherNode) {
        if (otherNode != null) {
            ObjectNode node = (ObjectNode)otherNode;
            Set<String> childrenNames = this.children.stream().map(BaseNode::getName).collect(Collectors.toSet());
            this.children.addAll(node.getChildren()
                    .stream()
                    .filter(n -> !childrenNames.contains(n.getName()))
                    .collect(Collectors.toList()));
        }
        return this;
    }
}
