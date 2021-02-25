package training.week8;

import com.wayue.olympus.common.ListUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Week8FZ implements Week8Worker {
	@Override
	public <T> Iterable<T> breadthFirst(T root, Function<T, List<T>> getChildren) {
		return traverse(root, getChildren, Queue::offer);
	}

	@Override
	public <T> Iterable<T> deepFirst(T root, Function<T, List<T>> getChildren) {
		return traverse(root, getChildren.andThen(ListUtil::reversed), Deque::push);
	}

	private <T> Iterable<T> traverse(T root, Function<T, ? extends Iterable<T>> getChildren, BiConsumer<Deque<T>, T> consumer) {
		Deque<T> deque = new LinkedList<>();
		deque.push(root);
		return () -> new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return !deque.isEmpty();
			}

			@Override
			public T next() {
				T next = deque.poll();
				getChildren.apply(next).forEach(t -> consumer.accept(deque, t));
				return next;
			}
		};
	}
}
