package training.week8;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Week8HX implements Week8Worker {
	public <T> Iterable<T> breadthFirst(T root, Function<T, List<T>> getChildren) {
		return () -> walkTree(root, getChildren, Deque::pollLast, Deque::peekLast);
//		return () -> walkTree(root, getChildren, Deque::offer);
	}

	public <T> Iterable<T> deepFirst(T root, Function<T, List<T>> getChildren) {
		return () -> walkTree(root, getChildren, Deque::poll, Deque::peek);
//		return () -> walkTree(root, getChildren.andThen(ListUtil::reversed), Deque::push);
	}

	@Deprecated
	public static <T> Iterator<T> walkTree(T root, Function<T, List<T>> getChildren, Function<Deque<T>, T> pollFunction, Function<Deque<T>, T> peekFunction) {
		return new Iterator<T>() {
			private T next = root;
			private Deque<T> deque = new LinkedList<>();
			private Set<T> visited = new HashSet<>();//optimize O(n) space complexity

			public boolean hasNext() {
				return next != null;
			}

			public T next() {
				deque.push(next);
				visited.add(next);
				T current = next;
				for (next = null; !deque.isEmpty(); pollFunction.apply(deque)) {
					next = getChildren.apply(peekFunction.apply(deque)).stream().filter(n -> !visited.contains(n)).findFirst().orElse(null);
					if (next != null) {
						break;
					}
				}
				return current;
			}
		};
	}

	public static <T> Iterator<T> walkTree(T root, Function<T, ? extends Iterable<T>> getChildren, BiConsumer<Deque<T>, T> biConsumer) {
		Deque<T> deque = new LinkedList<>();
		deque.push(root);
		return new Iterator<T>() {
			T next = root;

			public boolean hasNext() {
				return !deque.isEmpty();
			}

			public T next() {
				next = deque.poll();
				getChildren.apply(next).forEach(t -> biConsumer.accept(deque, t));//offer -> addLast, for breadthFirst
				return next;
			}
		};
	}

	public static int[] twoSum(int[] nums, int target) {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
		}
		for (int i = 0; i < nums.length; i++) {
			Integer index = map.getOrDefault(target - nums[i], null);
			if (index != null && index != i) {
				return new int[] { i, index };
			}
			map.put(nums[i], i);
		}
		return new int[] {};
	}

	public static void main(String[] args) {
		Arrays.asList("How are you", "Hello", "Tom", "Hi", "Hell", "Jerry")
				.stream()
				.filter(s -> s.startsWith("H"))
				.map(s -> s.substring(2))
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}
}