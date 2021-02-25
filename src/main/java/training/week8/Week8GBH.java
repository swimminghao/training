package training.week8;

import java.util.*;
import java.util.function.Function;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/9/22
 */
public class Week8GBH implements Week8Worker{

    @Override
    public <T> Iterable<T> breadthFirst(T root, Function<T, List<T>> getChildren) {
        List<T> result = new ArrayList<>();
        Queue<T> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            T tree = queue.poll();
            result.add(tree);
            List<T> apply = getChildren.apply(tree);
            if(apply != null) {
                queue.addAll(apply);
            }
        }
        return result;
    }

    @Override
    public <T> Iterable<T> deepFirst(T root, Function<T, List<T>> getChildren) {
        ArrayList<T> result = new ArrayList<>();
        Stack<T> stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()){
            T tree = stack.pop();
            result.add(tree);
            List<T> apply = getChildren.apply(tree);
            if(apply != null) {
                for (int i = apply.size() - 1; i >= 0; i--) {
                    stack.push(apply.get(i));
                }
            }
        }
        return result;
    }
}
