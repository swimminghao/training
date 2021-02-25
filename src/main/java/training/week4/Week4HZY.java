package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Week4HZY implements Week4Worker{
	@Override
	public <T> List<Collection<T>> sort(List<Pair<T, T>> relations){
		Map<T,Collection<T>> begin = new HashMap<>();
		for (Pair<T, T> pair : relations) {
			begin.computeIfAbsent(pair.getRight(), key -> new ArrayList<>()).add(pair.getLeft());
		}
		Set<T> nodes = relations.stream()
				.flatMap(a -> Stream.of(a.getLeft(),a.getRight()))
				.distinct().collect(Collectors.toSet());
		List<Collection<T>> sort = new ArrayList<>();
		List<T> first = nodes.stream().filter(a -> !begin.keySet().contains(a)).collect(Collectors.toList());
		sort.add(first);
		for (;!begin.isEmpty();){
			List<T> nowNodes = new ArrayList<>();
			List<T> doneNodes = sort.stream().flatMap(Collection::stream).collect(Collectors.toList());
			begin.forEach((key, value) -> {
				if (doneNodes.containsAll(value)) {
					nowNodes.add(key);
				}
			});
			nowNodes.forEach(begin::remove);
			sort.add(nowNodes);
		}
		return sort;
	}
}

