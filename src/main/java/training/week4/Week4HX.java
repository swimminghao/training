package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Week4HX implements Week4Worker {
	@Override
	public <T> List<Collection<T>> sort(List<Pair<T, T>> relations) {
		Map<T, Collection<T>> previous = new HashMap<>(), next = new HashMap<>();
		relations.forEach(r -> {
			previous.computeIfAbsent(r.getRight(), key -> new ArrayList<>()).add(r.getLeft());
			next.computeIfAbsent(r.getLeft(), key -> new ArrayList<>()).add(r.getRight());
		});
		Collection<T> candidates = relations.stream().flatMap(a -> Stream.of(a.getLeft(), a.getRight()))
				.filter(a -> !previous.keySet().contains(a)).collect(Collectors.toSet());
		List<Collection<T>> sorted = new ArrayList<>();
		Collection<T> completed = new HashSet<>();
		for (; !previous.isEmpty(); ) {
			sorted.add(candidates);
			completed.addAll(candidates);
			candidates.forEach(previous::remove);
			candidates = candidates.stream()
					.map(next::get)
					.filter(Objects::nonNull)
					.flatMap(Collection::stream)
					.filter(n -> completed.containsAll(previous.get(n)))
					.collect(Collectors.toSet());
			if (candidates.isEmpty() && !previous.isEmpty()) {
				throw new RuntimeException();
			}
		}
		return sorted;
	}
}