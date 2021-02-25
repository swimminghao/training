package training.week9;

import java.util.Comparator;

public interface Week9Worker {
	<T> Heap<T> getHeap(Comparator<T> comparator);

	interface Heap<T> {
		void offer(T t);

		T poll();
	}
}
