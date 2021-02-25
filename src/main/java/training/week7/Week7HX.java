package training.week7;

public class Week7HX implements Week7Worker {
	@Override
	public int[] intersections(int[] unions) {
		int[] intersections = new int[unions.length];
		for (int i = 1; i < unions.length; i++) {
			for (int j = i; j >= 0; j = (j > 0 ? (j - 1) & i : -1)) {
				intersections[i] += ((Integer.bitCount(j) & 1) != 0) ? unions[j] : -unions[j];
			}
		}
		return intersections;
	}

	@Override
	public int[] partitions(int[] unions) {
		int fullBits = (unions.length - 1);
		int[] partitions = new int[unions.length];
		int[] intersections = intersections(unions);
		for (int i = 1; i <= fullBits; i++) {
			for (int j = i ^ fullBits; j >= 0; j = (j > 0 ? (j - 1) & ~i & fullBits : -1)) {
				partitions[i] += ((Integer.bitCount(j ^ fullBits) - Integer.bitCount(i)) & 1) == 0 ? intersections[j ^ fullBits] : -intersections[j ^ fullBits];
			}
		}
		return partitions;
	}
}
