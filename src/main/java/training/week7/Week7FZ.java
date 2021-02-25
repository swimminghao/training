package training.week7;

public class Week7FZ implements Week7Worker {
	@Override
	public int[] intersections(int[] unions) {
		int[] intersections = new int[unions.length];
		for (int i = 1; i < unions.length; i++) {
			for (int j = 1; j < unions.length; j++) {
				if ((j & i) == j) {
					int sign = (Integer.bitCount(j) & 1) * 2 - 1;
					intersections[i] += sign * unions[j];
				}
			}
		}
		return intersections;
	}

	@Override
	public int[] partitions(int[] unions) {
		int[] intersections = intersections(unions);
		int[] partitions = new int[unions.length];
		for (int i = 1; i < unions.length; i++) {
			for (int j = 1; j < unions.length; j++) {
				if ((j & i) == i) {
					int sign = (Integer.bitCount(j ^ i) & 1) * 2 - 1;
					partitions[i] -= sign * intersections[j];
				}
			}
		}
		return partitions;
	}
}