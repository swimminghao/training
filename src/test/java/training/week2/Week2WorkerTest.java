package training.week2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class Week2WorkerTest {
	private Class<? extends Week2Worker> clazz;

	public Week2WorkerTest(Class<? extends Week2Worker> clazz) {
		this.clazz = clazz;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week2GJC.class, },
				{ Week2FZ.class, },
				{ Week2HX.class, },
				{ Week2XH.class },
		};
	}

	@Test
	public void work() throws ReflectiveOperationException {
		int cost = 0;
		Week2Worker worker = clazz.newInstance();
		for (Station start : Station.values()) {
			for (Station end : Station.values()) {
				Route route = worker.computeRoute(start, end);
				cost += route.getCostMinutes();
				System.out.println("cost = " + route.getCostMinutes() + ", \t" + route.getStations().stream().map(Enum::name).collect(Collectors.joining("->")));
			}
		}
		System.out.println("total-cost = " + cost);
	}
}
