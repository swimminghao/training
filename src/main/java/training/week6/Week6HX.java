package training.week6;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Week6HX implements Week6Worker {
	@Override
	public List<Pair<String, Integer>> work(List<Pair<String, String>> personToPaper, List<Pair<String, String>> paperToReference) {
		Map<String, Long> paperReferencedCountMap = paperToReference.stream()
				.collect(Collectors.groupingBy(Pair::getRight, Collectors.counting()));
		return personToPaper.stream()
				.collect(Collectors.groupingBy(Pair::getLeft)).entrySet().stream()
				.map(e -> Pair.of(e.getKey(), calcHIndex(e.getValue().stream().map(p -> paperReferencedCountMap.getOrDefault(p.getRight(), 0L)).collect(Collectors.toList()))))
				.sorted(Collections.reverseOrder(Comparator.comparingInt(Pair::getRight))).collect(Collectors.toList());

	}

	public static int calcHIndex(final List<Long> referencedCounts) {
		// todo-hx: 2019/9/9  use bucket sort
		referencedCounts.sort(Collections.reverseOrder(Comparator.comparing(Long::longValue)));
		int left = 0, right = referencedCounts.size() - 1;
		for (int index = (left + right) >> 1; right > left + 1; index = (left + right) >> 1) {
			index = referencedCounts.get(index) > index + 1 ? (left = index) : (right = index);
		}
		return referencedCounts.get(right) >= right + 1 ? right + 1 : right;
	}
}