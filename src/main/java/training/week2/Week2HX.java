package training.week2;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Week2HX implements Week2Worker {
	private static final Metro_HX metro = new Metro_HX();

	static {
		Arrays.asList(
				"A1(3)A2(4)A3(4)A4(5)A5",
				"B1(4)B2(3)B3(3)B4(4)B5(3)B6",
				"C1(5)C2(4)C3(3)C4(3)C5(4)C6(4)C7(3)C8",
				"A2(3)C3",
				"A4(3)B2",
				"B4(5)C5")
				.forEach(metro::append);
	}

	@Override
	public Route computeRoute(Station start, Station end) {
		Metro_HX.Route route = metro.calcRoute(start.name(), end.name());
		return new Route(route.getPath().stream().map(s -> Station.valueOf(s.getName())).collect(Collectors.toList()), (int) (double) route.getCost());
	}
}
