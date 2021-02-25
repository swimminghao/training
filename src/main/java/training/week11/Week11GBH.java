package training.week11;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/11/7
 */
public class Week11GBH implements Week11Worker{

    @Override
    public <T extends BinaryTreeNode<T>> String prettyPrintBinaryTree(T root) {
        return null;
    }

    @Override
    public <T extends TreeNode<T>> String prettyPrintTree(T root) {

        StringBuilder sb = new StringBuilder();
        List<Pair<T,List<Boolean>>> result = new ArrayList<>();
        Deque<Pair<T,List<Boolean>>> stack = new LinkedList<>();
        List<Boolean> first = new ArrayList<>();
        first.add(Boolean.FALSE);
        stack.push(Pair.of(root,first));
        while (!stack.isEmpty()) {
            Pair<T, List<Boolean>> item = stack.pop();
            result.add(item);
            T key = item.getKey();
            List<Boolean> value = item.getValue();
            List<T> children = key.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                for (int i = children.size() - 1; i >= 0; i--) {
                    List<Boolean> list1 = new ArrayList<>(value);
                    list1.add(i < children.size() - 1 ? Boolean.TRUE : Boolean.FALSE);
                    stack.push(Pair.of(children.get(i),list1));
                }
            }
        }

        for (Pair<T, List<Boolean>> item : result) {
            T key = item.getKey();
            boolean hasChild = CollectionUtils.isEmpty(key.getChildren());
            List<Boolean> values = item.getValue();
            List<String> texts = key.getTexts();
            for (int j = 0; j < texts.size(); j++) {
                if (values.size() > 1) {
                    for (int i = 1; i < values.size() - 1; i++) {
                        sb.append(values.get(i) ? "│    " : "     ");
                    }
                    String txt = values.get(values.size() - 1) ?
                            (j == 0 ? "├───╴•" :  hasChild ? "│     " : "│    │" ):
                            (j == 0 ? "╰───╴•" :  hasChild ? "      " : "     │");
                    sb.append(txt);
                } else {
                    sb.append(j == 0 ? "•" : "|");
                }
                sb.append(texts.get(j));
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }
}
