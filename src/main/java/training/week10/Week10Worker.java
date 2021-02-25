package training.week10;

import java.util.function.UnaryOperator;

public interface Week10Worker {
	int[] BSTGenerator(int n);

	<T> Iterable<T> BSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right);
}
