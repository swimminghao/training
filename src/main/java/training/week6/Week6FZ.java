package training.week6;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Week6FZ implements Week6Worker {
	private int hindex(List<Integer> counts) {
		int n = counts.size();
		int[] temp = new int[n + 1];
		for (int count : counts) {
			temp[Integer.min(count, n)]++;
		}
		for (int i = n, index = 0; ; i--) {
			if ((index += temp[i]) >= i) {
				return i;
			}
		}
	}

	@Override
	public List<Pair<String, Integer>> work(List<Pair<String, String>> personToPaper, List<Pair<String, String>> paperToReference) {
		List<Pair<String, Integer>> result = new ArrayList<>();
		Function<String, Integer> count = paper -> (int) paperToReference.stream().filter(p -> p.getRight().equals(paper)).count();
		for (Map.Entry<String, List<Integer>> entry : personToPaper.stream()
				.collect(Collectors.groupingBy(Pair::getLeft, Collectors.mapping(Pair::getRight, Collectors.mapping(count, Collectors.toList()))))
				.entrySet()) {
			List<Integer> counts = entry.getValue();
			int index = hindex(counts);
			result.add(Pair.of(entry.getKey(), index));
		}
		result.sort(Comparator.<Pair<String, Integer>, Integer>comparing(Pair::getRight).reversed());
		return result;
	}
}
