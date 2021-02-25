package training.week2;

import java.util.*;

/**
 * 地铁中任意两点的最优路径
 */
public class Week2XH implements Week2Worker {
	private Map<String, Map<String, Integer>> edges = new HashMap<>(); // 图的邻接矩阵，用二维数组表示
	private static final int MAX_WEIGHT = 99; // 设置最大权值，设置成常量
	private Collection<String> vertexes = new HashSet<>();// 按顺序保存顶点s
	private Map<String, Integer> dist = new HashMap<>();

	public Week2XH() {
		addEdge("A1", "A2", 3);
		addEdge("A2", "A3", 4);
		addEdge("A2", "C3", 3);
		addEdge("A3", "A4", 4);
		addEdge("A4", "A5", 5);
		addEdge("B1", "B2", 4);
		addEdge("B2", "B3", 3);
		addEdge("B2", "A4", 3);
		addEdge("B3", "B4", 3);
		addEdge("B4", "B5", 4);
		addEdge("B4", "C5", 5);
		addEdge("B5", "B6", 3);
		addEdge("C1", "C2", 5);
		addEdge("C2", "C3", 4);
		addEdge("C3", "C4", 3);
		addEdge("C3", "A2", 3);
		addEdge("C4", "C5", 3);
		addEdge("C5", "C6", 4);
		addEdge("C5", "B4", 5);
		addEdge("C6", "C7", 4);
		addEdge("C7", "C8", 3);
	}

	public int getWeight(String start, String stop) {
		if (start.equals(stop)) {
			return 0;
		}
		return edges.get(start).getOrDefault(stop, MAX_WEIGHT);
	} // 返<vi,vj>边的权值

	public void addEdge(String start, String stop, int weight) {
		edges.putIfAbsent(start, new HashMap<>());
		edges.putIfAbsent(stop, new HashMap<>());
		edges.get(start).putIfAbsent(stop, weight);
		edges.get(stop).putIfAbsent(start, weight);
		vertexes.add(start);
		vertexes.add(stop);
	}

	// 单元最短路径问题的Dijkstra算法
	private Map<String, String> dijkstra(String start) {
		Map<String, String> path = new HashMap<>(); // 存放从start到其他各点的最短路径的字符串表示
		for (String s : vertexes) {
			path.put(s, start + "-->" + s);
		}
		Map<String, Boolean> visited = new HashMap<>();
		// 初始化
		for (String s : vertexes) {
			dist.put(s, getWeight(start, s));
		}
		dist.put(start, 0);
		visited.put(start, true);
		for (int i = 1; i <= vertexes.size(); i++) {// 将所有的节点都访问到
			String visiting = dist.entrySet()
					.stream()
					.filter(e -> !visited.getOrDefault(e.getKey(), false))
					.min(Comparator.comparing(Map.Entry::getValue))
					.map(Map.Entry::getKey)
					.orElse(null);
			visited.put(visiting, true);// 将距离最近的节点加入已访问列表中
			for (String s : vertexes) {
				if (visited.getOrDefault(s, false)) {
					continue;
				}
				int newdist = dist.get(visiting) + getWeight(visiting, s);
				if (newdist < dist.get(s)) {
					dist.put(s, newdist);
					path.put(s, path.get(visiting) + "-->" + s);
				}
			}
			// update all new distance
		}// visite all nodes
		// for (int i = 0; i <= n; i++)
		// System.out.println("从" + vertex + "出发到" + i + "的最短路径为：" + path[i]);
		// System.out.println("=====================================");
		return path;
	}

	@Override
	public Route computeRoute(Station start, Station end) {
		String s1 = start.name();
		String s2 = end.name();
		if (!edges.containsKey(s1)) {
			return new Route(null, MAX_WEIGHT);
		}
		Map<String, String> path = dijkstra(s1);
		System.out.println("从" + start + "出发到" + end + "的最短路径为："
		                   + path.get(s2));
		List<Station> list = new ArrayList<>();
		String[] split = path.get(s2).split("-->");
		for (String s : split) {
			list.add(Station.valueOf(s));
		}
		return new Route(list, dist.get(s2));
	}

	public static void main(String[] args) {
		Week2XH week2XH = new Week2XH();
//		Scanner in = new Scanner(System.in);
//		System.out.println("请输入起始站点：");
//		String start = in.nextLine().trim();
//		System.out.println("请输入目标站点：");
//		String stop = in.nextLine().trim();
		Station s1 = Station.A1;//Station.valueOf(start);
		Station s2 = Station.B2;//Station.valueOf(stop);
		Route route = week2XH.computeRoute(s1, s2);// 包含自身站点
		System.out.println(s1 + " -> " + s2 + " 花费： " + route.getCostMinutes() + "分钟");
	}
}
