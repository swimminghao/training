package training.week6;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Week6GJC implements Week6Worker {
	@Override
	public List<Pair<String, Integer>> work(List<Pair<String, String>> personToPaper, List<Pair<String, String>> paperToReference) {

		HashMap<String, Integer> paperToReferenceNumMap = new HashMap<>(); // paper -> referenceNum
		for (Pair<String, String> pair : paperToReference) {
			paperToReferenceNumMap.put(pair.getRight(), paperToReferenceNumMap.getOrDefault(pair.getRight(), 0) + 1);
		}

		HashMap<String, List<Integer>> personToPaperReferenceNum = new HashMap<>(); // person -> person's each paper referenceNum
		for (Pair<String, String> pair : personToPaper) {
			List<Integer> paperReferenceNumList = personToPaperReferenceNum.getOrDefault(pair.getLeft(), new ArrayList<>());
			paperReferenceNumList.add(paperToReferenceNumMap.getOrDefault(pair.getRight(), 0));
			personToPaperReferenceNum.put(pair.getLeft(), paperReferenceNumList);
		}

		List<Pair<String, Integer>> personToindex = new ArrayList<>(); // person -> H index
		for (Map.Entry<String, List<Integer>> entry : personToPaperReferenceNum.entrySet()) {
			List<Integer> sorted = entry.getValue().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()); //从大到小排序
			int h = sorted.size();
			for (int i = 0; i < sorted.size(); i++) {
				if (sorted.get(i) <= i) {
					h = i;
					break;
				}
			}
			personToindex.add(Pair.of(entry.getKey(), h));
		}

		return personToindex;
	}

}
