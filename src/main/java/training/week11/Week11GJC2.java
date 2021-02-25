

package training.week11;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Week11GJC2 implements Week11Worker {
    final static String LINE_FEED = "\r\n";
    @Override
    public <T extends BinaryTreeNode<T>> String prettyPrintBinaryTree(T root) {
        ArrayDeque<Pair<T, Integer>> stack = new ArrayDeque<>();
        List<Pair<T, Integer>> list = new ArrayList<>();
        Set<T> set = new HashSet<>();
        Set<Integer> locationSet = new HashSet<>();
        stack.push(Pair.of(root, 0x1));
        T node;
        while (!stack.isEmpty()) {
            Pair<T, Integer> pair = stack.getFirst();
            node = pair.getLeft();
            int nodeValue = pair.getRight();
            T leftChild = node.getLeft();
            T rightChild = node.getRight();
            if (leftChild != null && !set.contains(leftChild)) {
                stack.push(Pair.of(leftChild, (nodeValue << 1) + 1));
            } else {
                stack.pop();
                set.add(node);
                if (rightChild != null && !set.contains(rightChild)) {
                    stack.push(Pair.of(rightChild, nodeValue << 1));
                }
                list.add(pair);
                locationSet.add(nodeValue);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Pair<T, Integer> p : list) {
            List<String> texts = new ArrayList<>(p.getLeft().getTexts());
            texts.add("");
            for (int j = 0; j < texts.size(); j++) {
                if (p.getRight() != 1) {
                    String bitString = Integer.toString(p.getRight(), 2);
                    char[] chars = bitString.toCharArray();
                    int bitNum = bitString.length();
                    for (int i = 1; i < bitNum - 1; i++) {
                        sb.append(chars[i] == chars[i + 1] ? " " : "│").append("   ");
                    }

                    int numberOfTrailingZeros = Integer.numberOfTrailingZeros(p.getRight());
                    if (j == 0) {
                        sb.append(numberOfTrailingZeros > 0 ? "╰" : "╭").append("───");
                    } else {
                        sb.append(numberOfTrailingZeros > 0 ? " " : "│").append("   ");
                    }
                }
                sb.append(j == 0 ? "•" : (locationSet.contains(p.getRight() * 2) ? "│" : " ")).append(texts.get(j))
                        //.append(" ").append(Integer.toString(p.getRight(), 2))
                        .append(LINE_FEED);
            }
        }
        return sb.toString();
    }


    @Override
    public <T extends TreeNode<T>> String prettyPrintTree(T root) {
        ArrayDeque<Pair<T, Integer>> stack = new ArrayDeque<>();
        List<Pair<T, Integer>> list = new ArrayList<>();
        stack.push(Pair.of(root, 0x1));
        Set<Integer> locationSet = new HashSet<>();
        T node;
        while (!stack.isEmpty()) {
            Pair<T, Integer> pair = stack.pop();
            node = pair.getLeft();
            int nodeValue = pair.getRight();
            locationSet.add(nodeValue);
            List<T> children = node.getChildren();
            if (children != null) {
                int index = 0;
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(Pair.of(children.get(i), (nodeValue << 1) + index++));
                }
            }
            list.add(pair);
        }

        StringBuilder sb = new StringBuilder();
        for (Pair<T, Integer> p : list) {
            List<String> texts = new ArrayList<>(p.getLeft().getTexts());
            texts.add("");
            for (int j = 0; j < texts.size(); j++) {
                if (p.getRight() != 1) {
                    String bitString = Integer.toString(p.getRight(), 2);
                    char[] chars = bitString.toCharArray();
                    int bitNum = bitString.length();
                    for (int i = 1; i < bitNum - 1; i++) {
                        sb.append(chars[i] != '1' ? " " : "│").append("   ");
                    }

                    if (Integer.numberOfTrailingZeros(p.getRight()) > 0) {
                        sb.append(j == 0 ? "╰" : " ");
                    } else {
                        if (locationSet.contains(p.getRight() - 1)) {
                            //存在同一个分支的子树
                            sb.append(j == 0 ? "├" : "│");
                        } else {
                            sb.append(j == 0 ? "╰" : " ");
                        }
                    }
                    sb.append(j == 0 ? "───" : "   ");
                }
                sb.append(j == 0 ? "•" : (locationSet.contains(p.getRight() * 2) ? "│" : " ")).append(texts.get(j))
                        //.append(" ").append(Integer.toString(p.getRight(), 2))
                        .append(LINE_FEED);
            }

        }
        return sb.toString();
    }
}
