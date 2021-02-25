package training.week10;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

public class Week10FZ implements Week10Worker {
	@Override
	public int[] BSTGenerator(int n) {
		int[] BST = new int[(1 << n) - 1];
		for (int i = 0, step = 1 << n; i < BST.length; step >>= 1) {
			for (int v = step / 2; v <= BST.length; v += step) {
				BST[i++] = v;
			}
		}
		return BST;
	}

	@Override
	public <T> Iterable<T> BSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right) {
		Deque<T> stack = new LinkedList<>();
		return () -> new Iterator<T>() {
			private T node = root;

			@Override
			public boolean hasNext() {
				return node != null || !stack.isEmpty();
			}

			@Override
			public T next() {
				for (; node != null; node = left.apply(node)) {
					stack.push(node);
				}
				T next = stack.pop();
				node = right.apply(next);
				return next;
			}
		};
	}
}
