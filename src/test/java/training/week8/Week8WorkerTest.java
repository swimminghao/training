package training.week8;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class Week8WorkerTest {
	private String chars = "12345";
	private int length = 4;
	private Function<String, List<String>> children = s -> s.length() >= length ? Collections.emptyList()
			: chars.chars()
			.mapToObj(c -> String.valueOf((char) c))
			.map(s::concat)
			.collect(Collectors.toList());
	private Week8Worker worker;

	public Week8WorkerTest(Class<? extends Week8Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week8FZ.class },
				{ Week8GBH.class },
				{ Week8GJC.class },
				{ Week8HX.class },
				{ Week8XH.class },
				};
	}

	@Test
	public void test() {
		test(worker::breadthFirst, s -> StringUtils.leftPad(s, length, "0"));
		test(worker::deepFirst, s -> StringUtils.rightPad(s, length, "0"));
	}

	private void test(BiFunction<String, Function<String, List<String>>, Iterable<String>> search, Function<String, String> preprocess) {
		int i = 0;
		String prev = null;
		for (String s : search.apply("", children)) {
			if (prev != null) {
				Assert.assertTrue(String.format("[%s] vs. [%s]", prev, s), Comparator.comparing(preprocess).compare(prev, s) < 0);
			}
			System.out.println(s);
			prev = s;
			i++;
		}
		int expected = ((int) Math.pow(chars.length(), length + 1) - 1) / (chars.length() - 1);
		Assert.assertEquals("wrong size", expected, i);
	}
}