package training.week6;

import org.apache.commons.lang3.tuple.Pair;
import training.week5.Week5Worker;

import java.util.*;
import java.util.stream.Collectors;

public class Week6ZJC implements Week6Worker {
	@Override
	public List<Pair<String, Integer>> work(List<Pair<String, String>> personToPaper, List<Pair<String, String>> paperToReference) {
		List<Pair<String, Integer>> list = new ArrayList<>();
		Map<String, Integer> paperReferencedCount = paperToReference.stream().collect(Collectors.groupingBy(Pair::getRight)).entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()));
		for (Map.Entry<String, List<Pair<String, String>>> e : personToPaper.stream()
				.collect(Collectors.groupingBy(Pair::getLeft)).entrySet()) {
			Integer[] counts = e.getValue().stream().map(p -> paperReferencedCount.getOrDefault(p.getRight(), 0)).toArray(Integer[]::new);
			Pair<String, Integer> of = Pair.of(e.getKey(), index(counts));
			list.add(of);
		}
		return list;
	}

	public int index(Integer[] counts){
		Arrays.sort(counts);
		int sum = 0;
		int h = 0;
		for (int i = counts.length -1; i >= 0; i--){
			h = counts[i];
			for (int j = counts.length-1; j >= 0; j--){
				if (counts[j] >= h)
					sum++;
			}
			if (sum >= h)
				break;
		}
		return h;
	}
}
