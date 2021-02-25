package training.week8;

import java.util.List;
import java.util.function.Function;

public interface Week8Worker {
	<T> Iterable breadthFirst(T root, Function<T, List<T>> getChildren);

	<T> Iterable<T> deepFirst(T root, Function<T, List<T>> getChildren);
}
