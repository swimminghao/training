package training.week7;

public class Week7HZY implements Week7Worker {
	public int[] intersections(int[] unions) {
		int[] intersections = new int[unions.length];
		for (int i = 0; i < unions.length; i++) {
			for (int j = 0; j < unions.length; j++) {
				if ((j | i) == i)
					intersections[i] += (Integer.bitCount(j) % 2 != 0) ? unions[j] : -unions[j];
			}
		}
		return intersections;
	}
	@Override
	public int[] partitions(int[] unions) {
		int[] partitions = new int[unions.length];
		int[] intersections = intersections(unions);
		for (int i = 1; i < unions.length; i++) {
			for (int j = 1; j < unions.length; j++){
			int sign =	((Integer.bitCount(j) % 2) == (Integer.bitCount(i) % 2))? 1: -1;
				if ((j & i) == i)
					partitions[i] += sign*intersections[j];
			}
		}
		return  partitions;
	}

}
