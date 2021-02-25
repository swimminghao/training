package training.week7;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
public class Week7WorkerTest {
	private static int max_n = 9, max_m = 9999;
	private static int[][] unions, intersections, partitions;
	private Week7Worker worker;

	public Week7WorkerTest(Class<? extends Week7Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	private static boolean check(int i, int position) {//检查i的第position位是不是1
		return (i >> position & 1) == 1;
	}

	@BeforeClass
	public static void init() {
		Map<Integer, Set<Integer>> relation = IntStream.range(0, max_n).boxed()//group -> element
				.collect(Collectors.toMap(Function.identity(), i -> IntStream.range(0, max_m).boxed()
						.filter(e -> Math.random() < .5)
						.collect(Collectors.toSet())));
		List<Set<Integer>> union = IntStream.range(0, 1 << max_n).boxed()
				.map(i -> relation.entrySet().stream()
						.filter(e1 -> check(i, e1.getKey()))
						.map(Map.Entry::getValue)
						.reduce(Sets::union)
						.orElse(Collections.emptySet()))
				.collect(Collectors.toList());
		List<Set<Integer>> intersection = IntStream.range(0, 1 << max_n).boxed()
				.map(i -> relation.entrySet().stream()
						.filter(e -> check(i, e.getKey()))
						.map(Map.Entry::getValue)
						.reduce(Sets::intersection)
						.orElse(Collections.emptySet()))
				.collect(Collectors.toList());
		unions = IntStream.range(0, max_n)
				.mapToObj(n -> IntStream.range(0, 1 << n)
						.map(i -> union.get(i).size())
						.toArray())
				.toArray(int[][]::new);
		intersections = IntStream.range(0, max_n)
				.mapToObj(n -> IntStream.range(0, 1 << n)
						.map(i -> intersection.get(i).size())
						.toArray())
				.toArray(int[][]::new);
		partitions = IntStream.range(0, max_n)
				.mapToObj(n -> IntStream.range(0, 1 << n)
						.map(i -> Sets.difference(intersection.get(i), union.get(((1 << n) - 1) ^ i)).size())
						.toArray())
				.toArray(int[][]::new);
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week7HX.class },
				{ Week7FZ.class },
				{ Week7GJC.class },
				};
	}

	@Test
	public void test() {
		for (int n = 0; n < max_n; n++) {
			System.out.printf("%n%s %s group(s) %1$s%n", String.join("", Collections.nCopies(50, "-")), n);
			for (int i = 0; i < 1 << n; i++) {
				String index = String.format("%" + Math.max(1, n) + "s", Integer.toBinaryString(i)).replace(' ', '0');
				System.out.printf("%s:  union = %04d,  intersection = %04d,  partition = %04d%n",
				                  index, unions[n][i], intersections[n][i], partitions[n][i]);
			}
			Assert.assertArrayEquals(n + " - intersection", intersections[n], worker.intersections(unions[n]));
			Assert.assertArrayEquals(n + " - partition", partitions[n], worker.partitions(unions[n]));
		}
	}
}