package training.week13.gujc;

import java.util.List;

public interface ComplexNode extends BaseNode {

    List<BaseNode> getChildren();

    void add(BaseNode node);

    ComplexNode merge(ComplexNode otherNode);
}
