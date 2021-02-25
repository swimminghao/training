package training.week9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Week9FZ implements Week9Worker {
	@Override
	public <T> Heap<T> getHeap(Comparator<T> comparator) {
		return new Heap<T>() {
			private List<T> array = new ArrayList<>();

			@Override
			public void offer(T t) {//sift up
				array.add(t);
				for (int c = array.size() - 1, p; c != 0; c = p) {
					p = c - 1 >> 1;
					if (comparator.compare(array.get(p), t) <= 0) {
						break;
					}
					array.set(c, array.set(p, t));
				}
			}

			@Override
			public T poll() {//sift down
				if (array.isEmpty()) {
					return null;
				} else if (array.size() == 1) {
					return array.remove(0);
				}
				T current = array.remove(array.size() - 1), result = array.set(0, current);
				for (int c = 0; ; ) {
					int l = 2 * c + 1, r = 2 * c + 2;
					if (array.size() <= l) {
						break;
					}
					T left = array.get(l);
					if (array.size() <= r) {
						if (0 < comparator.compare(current, left)) {
							array.set(c, array.set(l, current));
						}
						break;
					}
					T right = array.get(r);
					if (comparator.compare(current, left) <= 0 && comparator.compare(current, right) <= 0) {
						break;
					}
					int m = comparator.compare(left, right) < 0 ? l : r;
					array.set(c, array.set(m, current));
					c = m;
				}
				return result;
			}
		};
	}
}