package training.week4;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
public class Week4WorkerTest {
	private Week4Worker worker;

	public Week4WorkerTest(Class<? extends Week4Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week4FZ.class },
				{ Week4HX.class },
				{ Week4GJC.class },
				{ Week4ZJC.class },
				{ Week4HZY.class },
		};
	}

	@Test
	public void test() {
		char first = 'A', last = 'A' + 10;
		for (double d = 0; d < 1; d += .03) {
			List<Pair<String, String>> relations = new ArrayList<>();
			{
				List<Character> order = IntStream.rangeClosed(first, last).mapToObj(i -> (char) i).collect(Collectors.toList());
				Collections.shuffle(order);
				Comparator<Character> comparator = Comparator.comparing(order::indexOf);
				for (char i = first; i <= last; i++) {
					for (char j = first; j <= last; j++) {
						if (Math.random() < d && comparator.compare(i, j) < 0) {
							relations.add(Pair.of(String.valueOf(i), String.valueOf(j)));
						}
					}
				}
				Collections.shuffle(relations);
			}
			System.out.printf("input:\t%s%n", relations.stream().map(p -> String.format("%s→%s", p.getLeft(), p.getRight())).collect(Collectors.joining(", ")));
			List<Collection<String>> sorted = worker.sort(relations);
			{
				Map<String, Integer> order = new HashMap<>();
				for (int i = 0; i < sorted.size(); i++) {
					for (String s : sorted.get(i)) {
						order.put(s, i);
					}
				}
				Comparator<String> comparator = Comparator.comparing(order::get);
				for (Pair<String, String> relation : relations) {
					String l = relation.getLeft(), r = relation.getRight();
					Assert.assertTrue(String.format("relation not fulfil: %s -> %s", l, r), comparator.compare(l, r) < 0);
				}
			}
			System.out.printf("result:\t%s%n", sorted.stream().map(Collection::toString).collect(Collectors.joining(" → ")));
			Collection<String> previous = null;
			for (Collection<String> group : sorted) {
				if (previous != null) {
					for (String r : group) {
						Assert.assertTrue(String.format("[%s] could be earlier", r), previous.stream().anyMatch(l -> relations.stream().anyMatch(p -> p.getLeft().equals(l) && p.getRight().equals(r))));
					}
				}
				previous = group;
			}
			System.out.println(new String(new char[200]).replace('\0', '-'));
		}
	}
}