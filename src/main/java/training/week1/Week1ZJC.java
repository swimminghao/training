package training.week1;

public class Week1ZJC implements Week1Worker {
	@Override
	public int[][] a(int n, int m) {
		int A[][] = new int[m][n];
		int num1 = 0;
		for (int a = 0; a < n; a++) {
			if (a % 2 == 0) {
				for (int b = 0; b < m; b++) {
					A[b][a] = ++num1;
				}
			} else {
				for (int b = 0; b < m; b++) {
					A[m - 1 - b][a] = ++num1;
				}
			}
		}
		return A;
	}

	@Override
	public int[][] b(int n, int m) {
		int B[][] = new int[m][n];
		int num2 = 1;
		int i = 0, j = 0;
		for (int k = 0; k < n/4+1; k++) {
			for (i=0,j=2*k; i<m-2*k; i++){
				if (num2 > m * n)	break;
				B[i][j] = num2++;
			}
			for (i=m-2*k-1,j=2*k+1;j<n-2*k;j++){
				if (num2 > m * n)	break;
				B[i][j] = num2++;
			}
			for (j=n-2*k-1,i=m-2*k-2;i>=0;i--){
				if (num2 > m * n)	break;
				B[i][j] = num2++;
			}
			for (i=0,j=n-2*k-2;i<m-2*k-1;i++){
				if (num2 > m * n)	break;
				B[i][j] = num2++;
			}
			for (i=m-2*k-2,j=n-2*k-3;j>2*k;j--){
				if (num2 > m * n)	break;
				B[i][j] = num2++;
			}
			for (i=m-2*k-3,j=2*k+1;i>=0;i--){
				if (num2 > m * n)	break;
				B[i][j] = num2++;
			}
		}
		return B;
	}

	@Override
	public int[][] c(int n, int m) {
		int C[][] = new int[m][n];
		int num3 = 1;
		int i = 0, j = 0;
		int p = m < n ? m : n;
		for (int k = 0; k < p / 2 + 1; k++) {
			for (i = k, j = k; j < n - k - 1; j++) {
				if (num3 > m * n) {
					break;
				}
				C[i][j] = num3++;
			}
			for (i = k, j = n - k - 1; i < m - k - 1; i++) {
				if (num3 > m * n) {
					break;
				}
				C[i][j] = num3++;
			}
			for (i = m - k - 1, j = n - k - 1; j > k; j--) {
				if (num3 > m * n) {
					break;
				}
				C[i][j] = num3++;
			}
			for (i = m - k - 1, j = k; i > k; i--) {
				if (num3 > m * n) {
					break;
				}
				C[i][j] = num3++;
			}
		}
		if (m == n && m % 2 == 1) {
			C[m / 2][n / 2] = m * n;
		}
		return C;
	}
}
