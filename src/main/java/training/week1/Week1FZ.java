package training.week1;

public class Week1FZ implements Week1Worker {
	private static int[][] work(int n, int m, int[][] states) {//state = {row_direction, col_direction, 0:maintain step/1:next step}
		int[][] array = new int[n][m];
		for (int value = 0, r = 0, c = 0, s = 0; ; ) {
			array[r][c] = ++value;
			int rnext = r + states[s][0], cnext = c + states[s][1];
			if (rnext < 0 || n - 1 < rnext || cnext < 0 || m - 1 < cnext || array[rnext][cnext] != 0 || states[s][2] == 1) {
				s = (s + 1) % states.length;
				rnext = r + states[s][0];
				cnext = c + states[s][1];
				if (rnext < 0 || n - 1 < rnext || cnext < 0 || m - 1 < cnext || array[rnext][cnext] != 0) {
					break;
				}
			}
			r = rnext;
			c = cnext;
		}
		return array;
	}

	@Override
	public int[][] a(int n, int m) {
		return work(n, m, new int[][] { { 0, 1, 0 }, { 1, 0, 1 }, { 0, -1, 0 }, { 1, 0, 1 }, });
	}

	@Override
	public int[][] b(int n, int m) {
		return work(n, m, new int[][] { { 1, 0, 0 }, { 0, 1, 0 }, { -1, 0, 0 }, { 0, -1, 0 }, });
	}

	@Override
	public int[][] c(int n, int m) {
		return work(n, m, new int[][] { { 1, 0, 0 }, { 0, 1, 0 }, { -1, 0, 0 }, { 0, -1, 1 }, { 1, 0, 0 }, { 0, -1, 0 }, { -1, 0, 0 }, { 0, 1, 1 }, });
	}
}
