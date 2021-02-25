package training.week10;

import java.util.*;
import java.util.function.UnaryOperator;

public class Week10GJC implements Week10Worker {
    @Override
    public int[] BSTGenerator(int n) {
        //建立每层第一个数值的数组
        int[] firstValueArr = new int[n];
        firstValueArr[firstValueArr.length - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            firstValueArr[i] = firstValueArr[i + 1] * 2;
        }
        //按层、每层的位置，设置数值
        int[] arr = new int[(1 << n) - 1];
        arr[0] = firstValueArr[0];
        for (int i = 1; i < arr.length; i++) {
            int level = (int) (Math.log(i + 1) / Math.log(2));
            int indexOfLevel = i - (1 << level) + 1;
            arr[i] = firstValueArr[level] + firstValueArr[level - 1] * (indexOfLevel);
        }
        return arr;
    }

    @Override
    public <T> Iterable<T> BSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right) {

        return () -> new Iterator<T>() {
            ArrayDeque<T> stack = new ArrayDeque<>();
            Set<T> set = new HashSet<>();
            
            {
                stack.add(root);
            }

            @Override
            public boolean hasNext() {
                return stack.size() > 0;
            }

            @Override
            public T next() {
                T node;
                while (true) {
                    node = stack.getFirst();
                    T leftChild = left.apply(node);
                    T rightChild = right.apply(node);
                    if (leftChild != null && !set.contains(leftChild)) {
                        stack.push(leftChild);
                    } else {
                        node = stack.pop();
                        set.add(node);
                        if (rightChild != null && !set.contains(rightChild)) {
                            stack.push(rightChild);
                        }
                        break;
                    }
                }
                return node;
            }
        };
    }
}
