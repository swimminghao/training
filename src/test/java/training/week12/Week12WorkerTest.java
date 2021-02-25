package training.week12;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RunWith(Parameterized.class)
public class Week12WorkerTest {
	private Week12Worker worker;

	public Week12WorkerTest(Class<? extends Week12Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week12HX.class },
				};
	}

	@Test
	public void test() {
		Supplier<String> supplier = new Supplier<String>() {
			private int level;

			@Override
			public String get() {
				try {
					return StringUtils.repeat(Week12Worker.indent, level) + "'xxxxxxxxx'";
				} finally {
					double random = 10 * Math.random();
					if (random < 4) {
						level = (int) Math.min(level + 1, random);
					} else if (9 < random) {
						level++;
					}
				}
			}
		};
		List<String> lines = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			String line = supplier.get();
			System.out.println(line);
			lines.add(line);
		}
		System.out.println(StringUtils.repeat('-', 100));
		for (String s : worker.parse(lines)) {
			System.out.println(s);
		}
	}
}