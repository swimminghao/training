package training.week10;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Week10ZQ implements Week10Worker {

    @Override
    public int[] BSTGenerator(int n) {
        if (n < 1) {
            return null;
        }
        int initSize = (1 << n) - 1;
        int[] bstArray = new int[initSize];
        bstArray[0] = 1 << (n - 1);
        //每层数之前的差值初始值   例：n=4 第一层与第二层即根节点与左右节点 相差4      8
        //                                                                 4     12
        int layerDif = n == 1 ? 1 : 1 << (n - 2);
        for (int i = 0, initCount = 1;
             i < initSize
                     && (i * 2 + 1) < initSize
                     && (i * 2 + 2) < initSize; i++) {
            bstArray[i * 2 + 1] = bstArray[i] - layerDif;
            bstArray[i * 2 + 2] = bstArray[i] + layerDif;
            if ((1 << (initCount - 1) - 1 <= i
                    && i < ((1 << initCount) - 2)
                    && i != 0)) {
                continue;
            }
            initCount++;
            layerDif = layerDif / 2;
        }
        return bstArray;
    }


    /**
     * 中序遍历
     *
     * @param root
     * @param left
     * @param right
     * @param <T>
     * @return
     */
    @Override
    public <T> Iterable<T> BSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right) {
        return bSTIterator(root, left, right);
        /*if (null == root) {
            return null;
        }

        Collection<T> collection = new ArrayList();
        listBSTIterator(root, left, right, collection);
        return collection;
*/
    }


    /**
     * 递归实现
     *
     * @param root
     * @param left
     * @param right
     * @param collection
     * @param <T>
     */
    <T> void recursionBSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right,
                                  Collection<T> collection) {
        if (root == null) {
            return;
        }
        recursionBSTIterator(left.apply(root), left, right, collection);
        collection.add(root);
        recursionBSTIterator(right.apply(root), left, right, collection);
    }


    /**
     * 非递归实现
     *
     * @param root
     * @param left
     * @param right
     * @param collection
     * @param <T>
     */
    <T> void listBSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right,
                             Collection<T> collection) {
        if (root == null) {
            return;
        }
        Stack<T> st = new Stack();
        while (null != root || !st.empty()) {
            while (null != root) {
                st.push(root);
                root = left.apply(root);
            }
            if (!st.empty()) {
                root = st.pop();
                collection.add(root);
                root = right.apply(root);
            }
        }

    }

    /**
     * 迭代器
     *
     * @param root
     * @param left
     * @param right
     * @param collection
     * @param <T>
     */
    public <T> Iterable<T> bSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right) {
        return () -> new Iterator<T>() {
            private Deque<T> deque = new LinkedList();

            {
                T temRoot = root;
                test(temRoot, r -> null != r, deque, left);
            }

            @Override
            public boolean hasNext() {
                return !deque.isEmpty() && null != deque.peekFirst();
            }

            @Override
            public T next() {
                T next = deque.poll();
                T rightData = right.apply(next);
                test(rightData, r -> null != r, deque, left);
                return next;
            }
        };
    }


    private <T> void test(T t, Predicate<T> test, Deque deque, UnaryOperator<T> unaryOperator) {
        while (test.test(t)) {
            deque.push(t);
            t = unaryOperator.apply(t);
        }
    }
}
