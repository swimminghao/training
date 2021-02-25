package training.week1;

public class Week1HZY implements Week1Worker {
	@Override
	public int[][] a(int n, int m) {
		int i = 0;
		int arr[][] = new int[n][m];
		for (; i < n; ) {
			switch (i % 2) {
				case 0:
					for (int j = 0; j < m; j++) {
						arr[i][j] = i * m + j + 1;
					}
					i++;
					break;
				case 1:
					for (int j = 0; j < m; j++) {
						arr[i][j] = (i + 1) * m - j;
					}
					i++;
					break;
			}
		}
		return arr;
	}

	@Override
	public int[][] b(int n, int m) {
		int count = 1;
		int i = 0;
		int j = 0;
		int round = 0;
		int l;
		int arr[][] = new int[n][m];
		int k = Math.min(m, n);
		if (k == n) {
			l = i;
		} else {
			l = j;
		}
		for (; round <= k / 2 && count <= n * m; round++) {
			i = round;
			for (j = round; j < m - round && count <= n * m; j++) {
				arr[i][j] = count;
				count++;
			}
			j = m - round - 1;
			for (i = round + 1; i < n - round - 1 && count <= n * m; i++) {
				arr[i][j] = count;
				count++;
			}
			i = n - round - 1;
			for (j = m - round - 1; j >= round && count <= n * m; j--) {
				arr[i][j] = count;
				count++;
			}
			j = round;
			for (i = n - round - 2; i > round && count <= n * m; i--) {
				arr[i][j] = count++;
			}
		}
		if (m % 2 == 1 && n % 2 == 1 && n == m) {
			arr[n / 2][m / 2] = n * m;
		}
		return arr;
	}

	@Override
	public int[][] c(int n, int m) {
		int lift = 0;
		int right = 0;
		int pot = 0;
		int count = 1;
		int i = 0;
		int j = 0;
		int arr[][] = new int[n][m];
		for (; lift + right <= m && count <= n * m && pot <= n; pot++) {
			i = 0;
			j = lift + right;
			for (; i < n - lift - right && count <= n * m; i++) {
				arr[i][j] = count;
				count++;
			}
			i = n - lift - right - 1;
			for (j = right + lift + 1; j < m - right - lift && count <= n * m; j++) {
				arr[i][j] = count;
				count++;
			}
			j = m - right - lift - 1;
			for (i--; i >= 0 && count <= n * m; i--) {
				arr[i][j] = count;
				count++;
			}
			right++;
			i++;
			j = m - right - lift - 1;
			for (; i < n - right - lift - 1 && count <= n * m; i++) {
				arr[i][j] = count;
				count++;
			}
			i = n - right - lift - 1;
			for (; j > lift + right && count <= n * m; j--) {
				arr[i][j] = count;
				count++;
			}
			j = lift + right;
			for (; i >= 0 && count <= n * m; i--) {
				arr[i][j] = count;
				count++;
			}
			lift++;
			i++;
		}
		return arr;
	}
}
