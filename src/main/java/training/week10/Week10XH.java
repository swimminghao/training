package training.week10;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

/**
 * @author XH
 * @version 1.0
 * @Date 2019/10/30
 */
public class Week10XH implements Week10Worker {

    @Override
    public int[] BSTGenerator(int n) {
        int[] BST = new int[(1 << n) - 1];
        for (int layer = n - 1, i = 0; layer >= 0; layer--) {
            for (int j = 1 << layer; j < (1 << n); j = j + (1 << (layer + 1))) {
                BST[i++] = j;
            }
        }
        return BST;
    }

    @Override
    public <T> Iterable<T> BSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right) {
        return () -> new Iterator<T>() {
            Deque<T> stack = new LinkedList<>();
            T node = root;

            @Override
            public boolean hasNext() {
                return node != null || !stack.isEmpty();
            }

            @Override
            public T next() {
                while (node != null) {
                    stack.push(node);
                    node = left.apply(node);
                }
                T result = null;
                if (!stack.isEmpty()) {
                    result = stack.pop();
                    node = right.apply(result);
                }
                return result;
            }
        };
    }
}
