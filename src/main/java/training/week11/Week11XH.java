package training.week11;

import org.apache.commons.lang3.tuple.Pair;
import java.util.*;

public class Week11XH implements Week11Worker {

    int f1(int x) {
        int n = 0;
        while (x != 0) {
            x = x >> 1;
            n++;
        }
        return n;
    }

    @Override
    public <T extends BinaryTreeNode<T>> String prettyPrintBinaryTree(T root) {

        StringBuilder sb = new StringBuilder();
        for (Pair<T, Integer> p : inOrder(root)) {
            Integer position = p.getRight();
            List<String> texts = p.getLeft().getTexts();
            for (int i = 0; i < texts.size(); i++) {
                for (int j = f1(position) - 2; j >= 0; j--) {
                        boolean flag = (position & (1 << j)) != 0;
                        if (i == 0 && j == 1) {
                        sb.append(flag ? "╰───╴" : "╭───╴");
                    } else if (j != 0) {
                        boolean flag1 = (position & (1 << (j - 1))) != 0;
                        sb.append(flag == flag1 ? " " : "│").append("    ");
                    } else {
                        sb.append(i == 0 ? "•" : (j == 0) ? "│" : " ");
                    }
                }
                sb.append(texts.get(i)).append("\t" + Integer.toBinaryString(position)).append("\r\n");
            }
        }
        return sb.toString();
    }

    @Override
    public <T extends TreeNode<T>> String prettyPrintTree(T root) {
        StringBuilder sb = new StringBuilder();
        for (Pair<T, Integer> p : preOrder(root)) {
            Integer position = p.getRight();
            List<String> texts = p.getLeft().getTexts();
            for (int i = 0; i < texts.size(); i++) {
                for (int j = f1(position) - 2; j >= 0; j--) {
                    boolean flag = (position & (1 << j)) != 0;
                    if (i == 0 && j == 1) {
                        sb.append(flag ? "├───╴" : "╰───╴");
                    } else if (j != 0) {
                        sb.append(flag ? "│    " : "     ");
                    } else {
                        sb.append(i == 0 ? "•" : (j == 0) ? "│" : " ");
                    }
                }
                sb.append(texts.get(i)).append("\t" + Integer.toBinaryString(position)).append("\r\n");
            }
        }
        return sb.toString();
    }

    /**
     * 中序迭代
     *
     * @param root
     * @param <T>
     * @return
     */

    public <T extends BinaryTreeNode<T>> Iterable<Pair<T, Integer>> inOrder(T root) {
        return () -> new Iterator<Pair<T, Integer>>() {
            Deque<Pair<T, Integer>> stack = new LinkedList<>();
            Pair<T, Integer> node = Pair.of(root, 0b11);

            @Override
            public boolean hasNext() {
                return node != null || !stack.isEmpty();
            }

            @Override
            public Pair<T, Integer> next() {
                while (node != null) {
                    stack.push(node);
                    T leftChild = node.getLeft().getLeft();
                    int nodeValue = node.getRight();
                    node = leftChild == null ? null : Pair.of(leftChild, (nodeValue << 1) ^ 0b11);

                }
                Pair<T, Integer> result = stack.pop();
                T rightChild = result.getLeft().getRight();
                int nodeValue = result.getRight();
                node = rightChild == null ? null : Pair.of(rightChild, (nodeValue << 1) ^ 0b1);
                return result;
            }
        };
    }

    /**
     * 前序迭代
     *
     * @param root
     * @param <T>
     * @return
     */
    public <T extends TreeNode<T>> Iterable<Pair<T, Integer>> preOrder(T root) {
        Deque<Pair<T, Integer>> stack = new LinkedList<>();
        Pair<T, Integer> currentNode = Pair.of(root, 0b11);
        stack.push(currentNode);
        return () -> new Iterator<Pair<T, Integer>>() {

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public Pair<T, Integer> next() {
                Pair<T, Integer> nextNode = stack.pop();
                List<T> children = nextNode.getLeft().getChildren();
                int nodeValue = nextNode.getRight();
                if (children != null && !children.isEmpty()) {
                    stack.push(Pair.of(children.get(0), (nodeValue << 1) ^ 0b11));
                    stack.push(Pair.of(children.get(1), (nodeValue << 1) ^ 0b1));
                }
                return nextNode;
            }
        };
    }
}
