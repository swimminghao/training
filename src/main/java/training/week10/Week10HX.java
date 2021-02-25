package training.week10;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

public class Week10HX implements Week10Worker {
	@Override
	public int[] BSTGenerator(int n) {
		int[] bstArray = new int[(1 << n) - 1];
		for (int degree = n, index = 0; degree >= 0; degree--) {
			for (int c = 1 << degree; c <= (1 << n) - 1; c += 2 << degree) {
				bstArray[index++] = c;
			}
		}
		return bstArray;
	}

	@Override
	public <T> Iterable<T> BSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right) {
		return () -> new Iterator<T>() {
			Deque<T> stack = new LinkedList<>();

			{
				stack.push(root);
				for (T l = left.apply(root); l != null; l = left.apply(l)) {
					stack.push(l);
				}
			}

			@Override
			public boolean hasNext() {
				return !stack.isEmpty();
			}

			@Override
			public T next() {
				T top = stack.poll();
				for (T l = right.apply(top); l != null; l = left.apply(l)) {
					stack.push(l);
				}
				return top;
			}
		};
	}
}
