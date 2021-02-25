package training.week9;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;
import java.util.Comparator;

public class Week9XH implements Week9Worker {

    @Override
    public <T> Heap<T> getHeap(Comparator<T> comparator) {
        return new HeapDemo<>(comparator);
    }

    private static class HeapDemo<T> implements Week9Worker.Heap<T> {
        private Comparator<T> comparator;
        private int size = 0;
        private Object[] heap;

        HeapDemo(@NotNull Comparator<T> comparator) {
            this.comparator = comparator;
            this.heap = new Object[11];
        }

        @Override
        public void offer(T e) {
            if (e == null)
                throw new NullPointerException();
            int i = size;
            if (i >= heap.length)
                grow(i + 1);
            size = i + 1;
            if (i == 0)
                heap[0] = e;
            else
                siftUp(i, e);
        }

        @Override
        public T poll() {
            if (size == 0)
                return null;
            int s = --size;
            T result = (T) heap[0];
            T x = (T) heap[s];
            heap[s] = null;
            if (s != 0)
                siftDown(0, x);
            return result;
        }

        private void siftUp(int k, T x) {
            while (k > 0) {
                int parent = (k - 1) >> 1;
                Object e = heap[parent];
                if (comparator.compare(x, (T) e) >= 0)
                    break;
                heap[k] = e;
                k = parent;
            }
            heap[k] = x;
        }

        private void siftDown(int k, T x) {
            int half = size >> 1;
            while (k < half) {
                int child = (k << 1) + 1;
                Object c = heap[child];
                int right = child + 1;
                if (right < size &&
                        comparator.compare((T) c, (T) heap[right]) > 0)
                    c = heap[child = right];
                if (comparator.compare(x, (T) c) <= 0)
                    break;
                heap[k] = c;
                k = child;
            }
            heap[k] = x;
        }

        private static int hugeCapacity(int minCapacity) {
            if (minCapacity < 0)
                throw new OutOfMemoryError();
            return (minCapacity > Integer.MAX_VALUE - 8) ?
                    Integer.MAX_VALUE :
                    Integer.MAX_VALUE - 8;
        }

        private void grow(int minCapacity) {
            int oldCapacity = heap.length;
            int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                    (oldCapacity + 2) :
                    (oldCapacity >> 1));
            if (newCapacity - Integer.MAX_VALUE - 8 > 0)
                newCapacity = hugeCapacity(minCapacity);
            heap = Arrays.copyOf(heap, newCapacity);
        }
    }
}
