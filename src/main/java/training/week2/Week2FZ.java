package training.week2;

import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

import static training.week2.Station.*;

public class Week2FZ implements Week2Worker {
	private Map<Station, Map<Station, Integer>> edges = new HashMap<>();

	{
		for (Triple<Station, Station, Integer> triple : Arrays.asList(
				Triple.of(A1, A2, 3),
				Triple.of(A2, A3, 4),
				Triple.of(A3, A4, 4),
				Triple.of(A4, A5, 5),
				Triple.of(B1, B2, 4),
				Triple.of(B2, B3, 3),
				Triple.of(B3, B4, 3),
				Triple.of(B4, B5, 4),
				Triple.of(B5, B6, 3),
				Triple.of(C1, C2, 5),
				Triple.of(C2, C3, 4),
				Triple.of(C3, C4, 3),
				Triple.of(C4, C5, 3),
				Triple.of(C5, C6, 4),
				Triple.of(C6, C7, 4),
				Triple.of(C7, C8, 3),
				Triple.of(A2, C3, 3),
				Triple.of(A4, B2, 3),
				Triple.of(B4, C5, 5))) {
			Station s1 = triple.getLeft(), s2 = triple.getMiddle();
			int cost = triple.getRight();
			edges.computeIfAbsent(s1, k -> new HashMap<>()).put(s2, cost);
			edges.computeIfAbsent(s2, k -> new HashMap<>()).put(s1, cost);
		}
	}

	@Override
	public Route computeRoute(Station start, Station end) {
		Map<Station, Integer> costs = new HashMap<>();
		costs.put(start, 0);
		Queue<Station> queue = new LinkedList<>();
		queue.offer(start);
		Map<Station, Station> previous = new HashMap<>();
		for (Station source = queue.poll(); source != end && source != null; source = queue.poll()) {
			for (Map.Entry<Station, Integer> edge : edges.get(source).entrySet()) {
				Station target = edge.getKey();
				int cost = costs.get(source) + edge.getValue();
				if (!costs.containsKey(target) || cost < costs.get(target)) {
					costs.put(target, cost);
					previous.put(target, source);
					queue.offer(target);
				}
			}
		}
		if (!costs.containsKey(end)) {
			return new Route(Collections.emptyList(), -1);
		}
		LinkedList<Station> path = new LinkedList<>();
		for (Station s = end; s != null; s = previous.get(s)) {
			path.addFirst(s);
		}
		return new Route(path, costs.get(end));
	}
}
