package training.week8;

import java.util.*;
import java.util.function.Function;

public class Week8GJC implements Week8Worker {
    @Override
    public <T> Iterable<T> breadthFirst(T root, Function<T, List<T>> getChildren) {
        Deque<T> queue = new ArrayDeque<>();
        queue.add(root);
        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return queue.size() > 0;
            }
            @Override
            public T next() {
                T node = queue.poll();
                List<T> nodes = getChildren.apply(node);
                if (nodes != null) {
                    queue.addAll(nodes);
                }
                return node;
            }
        };
    }

    @Override
    public <T> Iterable<T> deepFirst(T root, Function<T, List<T>> getChildren) {
        Deque<T> stack = new ArrayDeque<>();
        stack.add(root);
        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return stack.size() > 0;
            }
            @Override
            public T next() {
                    T node = stack.pop();
                    List<T> nodes = getChildren.apply(node);
                    if (nodes != null) {
                        for (int i = nodes.size() - 1; i >= 0; i--) {
                            stack.push(nodes.get(i));
                        }
                    }
                return node;
            }
        };
    }
}
