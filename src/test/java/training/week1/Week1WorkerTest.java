package training.week1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
public class Week1WorkerTest {
	private Class<? extends Week1Worker> clazz;

	public Week1WorkerTest(Class<? extends Week1Worker> clazz) {
		this.clazz = clazz;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week1GJC.class, },
				};
	}

	@Test
	public void work() throws ReflectiveOperationException {
		Week1Worker worker = clazz.newInstance();
		for (int n = 5; n < 10; n++) {
			for (int m = 5; m < 10; m++) {
				System.out.printf("n = %d, m = %d.%n", n, m);
				for (int[][] arrays : Arrays.asList(worker.a(n, m), worker.b(n, m), worker.c(n, m))) {
					String output = Arrays.stream(arrays)
							.map(row -> IntStream.of(row)
									.mapToObj(c -> String.format("%02d", c))
									.collect(Collectors.joining("\t")))
							.collect(Collectors.joining("\n"));
					System.out.println(output);
					System.out.println(new String(new char[50]).replace('\0', '-'));
				}
				System.out.println(new String(new char[100]).replace('\0', '-'));
			}
		}
	}
}
