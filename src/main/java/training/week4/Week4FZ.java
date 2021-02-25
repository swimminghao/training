package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Week4FZ implements Week4Worker {
	@Override
	public <T> List<Collection<T>> sort(List<Pair<T, T>> relations) {
		List<Collection<T>> result = new ArrayList<>();
		Map<T, Set<T>> previous = relations.stream()
				.collect(Collectors.groupingBy(Pair::getRight, Collectors.mapping(Pair::getLeft, Collectors.toSet())));
		Map<T, Set<T>> next = relations.stream()
				.collect(Collectors.groupingBy(Pair::getLeft, Collectors.mapping(Pair::getRight, Collectors.toSet())));
		Collection<T> currents = next.keySet().stream().filter(k -> !previous.containsKey(k)).collect(Collectors.toSet());
		for (; !currents.isEmpty(); ) {
			Set<T> temp = new HashSet<>();
			for (T current : currents) {
				for (T n : next.getOrDefault(current, Collections.emptySet())) {
					previous.get(n).remove(current);
					if (previous.get(n).isEmpty()) {
						temp.add(n);
						previous.remove(n);
					}
				}
			}
			result.add(currents);
			currents = temp;
		}
		if (!previous.isEmpty()) {
			throw new RuntimeException("circular dependency: " + previous);
		}
		return result;
	}
}