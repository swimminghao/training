package training.week11;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Copyright (C), 2018-2019 善诊
 * Week11ZQ
 *
 * @Author: zhuqiang
 * @Date: 2019-11-01
 */
public class Week11ZQ implements Week11Worker {

    private final static String leftChar = "╭───╴";
    private final static String rightChar = "╰───╴";
    private final static String spaceLength = "     ";
    private final static String data = "•%s";
    private final static String mid = "│";
    private final static String changeLine = "\n";
    private final static String childrenChar = "├───╴";
    private final static Integer dataTag = 1;
    private final static Integer leftChildTag = 2;
    private final static Integer rightChildTag = 3;
    private final static Integer midTag = 4;
    private final static Integer spaceTag = 0;

    @Override
    public <T extends BinaryTreeNode<T>> String prettyPrintBinaryTree(T root) {
        return buildString(bSTIterator(root).iterator(), leftChar);


    }

    @Override
    public <T extends TreeNode<T>> String prettyPrintTree(T root) {
        return buildString(frontIterator(root).iterator(), childrenChar);
    }

    /**
     * 中序迭代
     *
     * @param root
     * @param left
     * @param right
     * @param collection
     * @param <T>
     */
    public <T extends BinaryTreeNode<T>> Iterable<Pair<T, List<Integer>>> bSTIterator(T root) {
        return () -> new Iterator<Pair<T, List<Integer>>>() {
            private Deque<Pair<T, List<Integer>>> deque = new LinkedList();
            private Pair<T, List<Integer>> rootPair
                    = Pair.of(root, Arrays.asList(1));

            @Override
            public boolean hasNext() {
                return !deque.isEmpty() || null != rootPair;
            }


            @Override
            public Pair<T, List<Integer>> next() {
                while (null != rootPair) {
                    deque.push(rootPair);
                    T left = rootPair.getKey().getLeft();
                    List pairLeftList = childrenList(rootPair, Arrays.asList(0, 2, 1));
                    rootPair = left == null ? null : Pair.of(left, pairLeftList);
                }
                Pair<T, List<Integer>> nextPair = deque.pop();
                List pairRightList = childrenList(nextPair, Arrays.asList(0, 3, 1));
                T right = nextPair.getKey().getRight();
                rootPair = right == null ? null : Pair.of(right, pairRightList);
                return nextPair;

            }
        };
    }

    /**
     * 前序迭代
     *
     * @param root
     * @param left
     * @param right
     * @param collection
     * @param <T>
     */
    public <T extends TreeNode<T>> Iterable<Pair<T, List<Integer>>> frontIterator(T root) {
        return () -> new Iterator<Pair<T, List<Integer>>>() {
            private Deque<Pair<T, List<Integer>>> deque = new LinkedList();
            private Pair<T, List<Integer>> rootPair
                    = Pair.of(root, Arrays.asList(1));

            @Override
            public boolean hasNext() {
                return !deque.isEmpty() || null != rootPair;
            }

            @Override
            public Pair<T, List<Integer>> next() {
                List<T> children = rootPair.getKey().getChildren();
                int listSize = rootPair.getRight().size();

                List<Integer> parentCharList = rootPair.getRight()
                        .subList(0, listSize > 2 ? listSize - 2 : listSize - 1);

                List pairList =
                        union(parentCharList
                                , rootPair.getRight().contains(2)
                                        ? Arrays.asList(4, 2, 1) : Arrays.asList(0, 2, 1));

                List finalPairList =
                        union(parentCharList
                                , rootPair.getRight().contains(3)
                                        ? Arrays.asList(0, 3, 1) : Arrays.asList(4, 3, 1));

                for (int i = children.size() - 1; i >= 0; i--) {
                    Pair rightPair;
                    if (i == children.size() - 1) {
                        rightPair = Pair.of(children.get(i), finalPairList);
                    } else {
                        rightPair = Pair.of(children.get(i), pairList);
                    }
                    deque.push(rightPair);
                }
                Pair<T, List<Integer>> nextPair = rootPair;
                if (deque.isEmpty()) {
                    rootPair = null;
                    return nextPair;
                }
                rootPair = deque.pop();
                return nextPair;
            }
        };
    }


    /**
     * 两个集合合集
     *
     * @param lists
     * @return
     */
    private List<Integer> union(List<Integer>... lists) {
        List unionList = new ArrayList();
        for (List temList : lists) {
            if (null == temList || temList.isEmpty()) {
                continue;
            }
            unionList.addAll(temList);
        }
        return unionList;
    }

    /**
     * @param rootPair
     * @param childList
     * @param <T>
     * @return
     */
    private <T> List<Integer> childrenList(Pair<T, List<Integer>> rootPair, List childList) {

        if (rootPair.getRight().contains(2) && childList.contains(3)) {
            childList = Arrays.asList(4, 3, 1);
        }
        if (rootPair.getRight().contains(3) && childList.contains(2)) {
            childList = Arrays.asList(4, 2, 1);
        }
        int listSize = rootPair.getRight().size();
        List pairRightList =
                union(rootPair.getRight().subList(0, listSize > 2 ? listSize - 2 : listSize - 1)
                        , childList);

        return pairRightList;
    }

    /**
     * 拼接string
     *
     * @param pairs
     * @param changeChar
     * @param <T>
     * @return
     */
    private <T extends TreeNode<T>> String buildString(Iterator<Pair<T, List<Integer>>> pairs, String changeChar) {
        StringBuilder stringBuilder = new StringBuilder();
        while (pairs.hasNext()) {
            Pair<T, List<Integer>> listPair = pairs.next();
            String dataString = String.format(data, listPair.getKey().getTexts().get(0));
            List<Integer> integers = listPair.getRight();
            if (integers.size() > 1) {
                integers = integers.subList(1, listPair.getRight().size());
            }
            for (Integer i : integers) {
                if (dataTag.equals(i)) {
                    stringBuilder.append(dataString);
                }
                if (spaceTag.equals(i)) {
                    stringBuilder.append(spaceLength);
                }
                if (leftChildTag.equals(i)) {
                    stringBuilder.append(changeChar);
                }
                if (rightChildTag.equals(i)) {
                    stringBuilder.append(rightChar);
                }
                if (midTag.equals(i)) {
                    stringBuilder.append(mid).append(spaceLength.substring(1));
                }
            }
            stringBuilder.append(changeLine);

        }
        return stringBuilder.toString();
    }

}
