package training.week9;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/10/18
 */
public class Week9GBH implements Week9Worker{


    @Override
    public <T> Heap<T> getHeap(Comparator<T> comparator) {
        return new DefaultHeap<>(comparator);
    }

    static class DefaultHeap<T> implements Week9Worker.Heap<T>{

        private List<T> elements = new ArrayList<>();
        private Comparator<T> comparator;

        DefaultHeap(@NonNull Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public void offer(T t) {
            elements.add(t);
            up();
        }

        @Override
        public T poll() {
            if (elements.size() == 1) {
                return elements.remove(0);
            } else if (elements.size() > 1) {
                T last = elements.remove(elements.size()-1);

                T first = elements.set(0,last);
                down();
                return first;
            }
            return null;
        }

        private void down() {
            for (int i = 0; i < elements.size();) {
                int leftIndex = i * 2 + 1;
                int rightIndex = i * 2 + 2;
                T t = null;
                int index = i;
                if (leftIndex < elements.size() && rightIndex < elements.size()) {
                    index = comparator.compare(elements.get(leftIndex), elements.get(rightIndex)) < 0 ? leftIndex : rightIndex;
                    t = elements.get(index);
                } else if (leftIndex < elements.size()) {
                    t = elements.get(leftIndex);
                    index = leftIndex;
                }

                if (null != t && comparator.compare(elements.get(i),elements.get(index)) > 0) {
                    elements.set(i,elements.set(index, elements.get(i)));
                    i = index;
                } else {
                    break;
                }
            }
        }

        private void up() {
            for (int i = elements.size() - 1; i >= 0; ) {
                int parentIndex = (i + 1) / 2 - 1;
                if (parentIndex < 0) {
                    break;
                }
                if (comparator.compare(elements.get(i),elements.get(parentIndex)) < 0) {
                    elements.set(i,elements.set(parentIndex, elements.get(i)));
                    i = parentIndex;
                }else{
                    break;
                }
            }

        }
    }
}
