package training.week8;

import java.util.*;
import java.util.function.Function;
/**
 * @author XH
 * @version 1.0
 * @Date 2019/9/30
 */
public class Week8XH implements Week8Worker {
    @Override
    public <T> Iterable<T> breadthFirst(T root, Function<T, List<T>> getChildren) {
        Queue<T> queue = new LinkedList<>();
        Set<T> visited = new HashSet<>();
        queue.add(root);
        visited.add(root);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return !queue.isEmpty();
                    }

                    @Override
                    public T next() {
                        T current = queue.remove();
                        List<T> nodes = getChildren.apply(current);
                        for (T neighbor : nodes) {
                            if (visited.add(neighbor)) {
                                queue.add(neighbor);
                            }
                        }
                        return current;
                    }
                };
            }
        };
    }

    @Override
    public <T> Iterable<T> deepFirst(T root, Function<T, List<T>> getChildren) {
        Stack<T> stack = new Stack<T>();
        stack.push(root);
        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                T current = stack.pop();
                List<T> children = getChildren.apply(current);
                if (children != null && !children.isEmpty()) {
                    Collections.reverse(children);
                    for (T child : children) {
                        stack.push(child);
                    }
                }
                return current;
            }
        };
    }
}
