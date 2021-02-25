package training.week9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Week9GJC implements Week9Worker {
    @Override
    public <T> Heap<T> getHeap(Comparator<T> comparator) {
        return new Heap<>(comparator);
    }

    class Heap<T> implements Week9Worker.Heap<T> {
        private List<T> list = new ArrayList<>();
        private Comparator<T> comparator;

        Heap(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public void offer(T t) {
            T element = t;
            list.add(element);
            for (int index = list.size() - 1; index != 0; index = (index - 1) / 2) {
                int parentIndex = (index - 1) / 2;
                T parent = list.get(parentIndex);
                element = list.get(index);
                if (comparator.compare(parent, element) > 0) {
                    list.set(index, parent);
                    list.set(parentIndex, element);
                } else {
                    break;
                }
            }
        }

        @Override
        public T poll() {
            if (list.size() == 0) {
                return null;
            }
            T top = list.get(0);
            int index = list.size() - 1;
            T t = list.get(index);
            list.set(0, t);
            list.remove(index);
            index = 0;
            while (true) {
                int leftIndex = 2 * index + 1;
                int rightIndex = 2 * index + 2;
                if (leftIndex >= list.size()) {
                    break;
                }
                T left = list.get(leftIndex);
                T right = rightIndex >= list.size() ? null : list.get(rightIndex);
                if ((rightIndex >= list.size() || comparator.compare(left, right) < 0) && comparator.compare(t, left) > 0) {
                    list.set(leftIndex, t);
                    list.set(index, left);
                    index = leftIndex;
                } else if (rightIndex < list.size() && comparator.compare(t, right) > 0) {
                    list.set(rightIndex, t);
                    list.set(index, right);
                    index = rightIndex;
                } else {
                    break;
                }
            }
            return top;
        }
    }
}
