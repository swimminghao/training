package training.week13.gujc;


import com.wayue.olympus.common.TreeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public abstract class AbstractNode implements BaseNode {
    @Getter
    String name;
    @Getter
    NodeType type;

    @Override
    public String prettyPrint() {
        Function<BaseNode, List<String>> texts = n -> Stream.concat(
                Stream.of(n.toString().replaceAll(".*\\(|\\)", "").split(", ")),
                Stream.of("")
        ).collect(Collectors.toList());
//		Function<SchemaNode, List<String>> texts = n -> Arrays.asList(n.toString(), "");
        Function<BaseNode, List<BaseNode>> children = n -> n instanceof ComplexNode ?
                ((ComplexNode) n).getChildren().stream().sorted(Comparator.comparing(BaseNode::getName)).collect(Collectors.toList())
                : Collections.emptyList();
        return TreeUtil.prettyPrintTree(this, children, texts);
    }


    public String toString() {
        return getName() + " [" + getType().toString() + "]";
    }
}
