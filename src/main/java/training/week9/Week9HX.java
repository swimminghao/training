package training.week9;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

public class Week9HX implements Week9Worker {
	@Override
	public <T> Heap<T> getHeap(Comparator<T> comparator) {
		return new HeapImpl<>(comparator);
	}

	private static class HeapImpl<T> implements Week9Worker.Heap<T> {
		private Comparator<T> comparator;
		private ArrayList<T> elements;

		HeapImpl(@NotNull Comparator<T> comparator) {
			this.comparator = comparator;
			elements = new ArrayList<>();
		}

		@Override
		public void offer(T t) {
			elements.add(t);
			for (int index = elements.size() - 1; index != 0; ) {
				int parentIndex = (index + 1) / 2 - 1;
				if (comparator.compare(elements.get(index), elements.get(parentIndex)) < 0) {
					elements.set(index, elements.set(parentIndex, elements.get(index)));
					index = parentIndex;
				} else {
					break;
				}
			}
		}

		@Override
		public T poll() {
			if (elements.size() < 1) {
				return null;
			} else if (elements.size() == 1) {
				return elements.remove(0);
			} else {
				T top = elements.set(0, elements.remove(elements.size() - 1));
				for (int index = 0; ; ) {
					int leftIndex = 2 * index + 1, rightIndex = 2 * index + 2;
					if (leftIndex >= elements.size()) {
						break;
					} else if (rightIndex >= elements.size()) {
						if (comparator.compare(elements.get(index), elements.get(leftIndex)) > 0) {
							elements.set(index, elements.set(leftIndex, elements.get(index)));
						}
						break;
					} else {
						int minIndex = comparator.compare(elements.get(leftIndex), elements.get(rightIndex)) <= 0 ? leftIndex : rightIndex;
						if (comparator.compare(elements.get(index), elements.get(minIndex)) > 0) {
							elements.set(index, elements.set(minIndex, elements.get(index)));
							index = minIndex;
						} else {
							break;
						}
					}
				}
				return top;
			}
		}
	}
}
