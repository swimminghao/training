package training.week2;

import lombok.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class Metro_HX {
	@Getter
	@Setter
	@RequiredArgsConstructor
	public static class Station {
		private final String name;
		private Map<Station, Double> children = new HashMap<>();

		@Override
		public String toString() {
			return name;
		}
	}

	@Setter
	@Getter
	@AllArgsConstructor
	public static class Route {
		Double cost;
		List<Station> path;

		@Override
		public String toString() {
			return String.format("route\t:\t[%s]\r\ncost\t:\t[%s]", path.stream().map(Station::getName).collect(Collectors.joining("➡")), cost);
		}
	}

	private Map<String, Station> stations = new HashMap<>();

	public boolean append(String line) {
		String previous = null;
		List<Pair<String, String>> edges = new ArrayList<>();
		for (String station : line.split("\\(\\d+(\\.\\d+)?\\)")) {
			if (previous != null) {
				edges.add(Pair.of(previous, station));
			}
			previous = station;
		}
		for (Pair<String, String> edge : edges) {
			Matcher matcher = Pattern.compile(edge.getLeft() + "\\((?<dis>\\d+(\\.\\d+)?)\\)" + edge.getRight()).matcher(line);
			if (matcher.find()) {
				Double dis = Double.valueOf(matcher.group("dis"));
				String leftName = edge.getLeft();
				Station leftStation = stations.getOrDefault(leftName, new Station(leftName));
				String rightName = edge.getRight();
				Station rightStation = stations.getOrDefault(rightName, new Station(rightName));
				leftStation.getChildren().putIfAbsent(rightStation, dis);
				rightStation.getChildren().putIfAbsent(leftStation, dis);
				stations.putIfAbsent(leftName, leftStation);
				stations.putIfAbsent(rightName, rightStation);
			} else {
				throw new RuntimeException("cannot match distance from " + edge.getLeft() + " to " + edge.getRight());
			}
		}
		return true;
	}

	public Route calcRoute(String begin, String end) {
		return calcRoute(stations.get(begin), stations.get(end));
	}

	public Route calcRoute(Station begin, Station end) {
		if (!stations.values().contains(begin) || !stations.values().contains(end)) {
			throw new RuntimeException(String.format("begin station or end station is not in metro system : begin = [%s] end = [%s]", begin, end));
		}
		if (begin == end) {
			return new Route(0d, Collections.singletonList(begin));
		}
		Map<Station, Double> visited = new HashMap<>();
		Map<Station, Double> unvisited = new HashMap<>();
		Map<Station, List<Station>> paths = new HashMap<>();
		for (Map.Entry<String, Station> entry : stations.entrySet()) {
			Station station = entry.getValue();
			unvisited.put(station, station.equals(begin) ? 0d : begin.getChildren().getOrDefault(station, Double.MAX_VALUE));
			paths.put(station, new ArrayList<>());
			paths.get(station).add(begin);
			paths.get(station).add(begin.getChildren().keySet().contains(station) ? station : null);
		}
		unvisited.remove(begin);
		visited.put(begin, 0d);
		paths.remove(begin);
		for (; ; ) {
			Station nearest = null;
			double mindis = Double.MAX_VALUE;
			for (Map.Entry<Station, Double> entry : unvisited.entrySet()) {
				Double dis = entry.getValue();
				if (dis < mindis) {
					mindis = dis;
					nearest = entry.getKey();
				}
			}
			if (nearest == null || visited.keySet().contains(end)) {
				return new Route(visited.get(end), paths.get(end));
			}
			unvisited.remove(nearest);
			visited.put(nearest, mindis);
			for (Map.Entry<Station, Double> entry : nearest.getChildren().entrySet()) {
				Station child = entry.getKey();
				if (unvisited.containsKey(child)) {
					Double oldDis = unvisited.get(child);
					double newDis = visited.get(nearest) + entry.getValue();
					if (oldDis > newDis) {
						unvisited.put(child, newDis);
						ArrayList<Station> stations = new ArrayList<>(paths.get(nearest));
						stations.add(child);
						paths.put(child, stations);
					}
				}
			}
		}
	}
}
