package training.week1;

public class Week1XH implements Week1Worker {
	@Override
	public int[][] a(int n, int m) {
		return initArray(n, m, Direction.Right, (snake, direction, row, col) -> {
			switch (direction) {
				case Right:
					if ((col == snake[0].length - 1) || (snake[row][col + 1] != 0)) {
						direction = Direction.Down;
					}
					break;
				case Down:
					if ((row == snake.length - 1) || (snake[row + 1][col] != 0)) {
						direction = Direction.Left;
					}
					break;
				case Left:
					if ((col == 0) || snake[row][col - 1] != 0) {
						direction = Direction.Up;
					}
					break;
				case Up:
					if (snake[row - 1][col] != 0) {
						direction = Direction.Right;
					}
					break;
			}
			return direction;
		});
	}

	static enum Direction {
		Right, Down, Left, Up
	}

	@Override
	public int[][] b(int n, int m) {
		return initArray(n, m, Direction.Right, (snake, direction, row, col) -> {
			switch (direction) {
				case Right:
					if ((col == m - 1) || (snake[row][col + 1] != 0)) {
						direction = Direction.Down;
					}
					break;
				case Down:
					if (col == m - 1) {
						direction = Direction.Left;
					}
					if (col == 0) {
						direction = Direction.Right;
					}
					break;
				case Left:
					if ((col == 0) || snake[row][col - 1] != 0) {
						direction = Direction.Down;
					}
					break;
				case Up:
					if (snake[row - 1][col] != 0) {
						direction = Direction.Right;
					}
					break;
			}
			return direction;
		});
	}

	@Override
	public int[][] c(int n, int m) {
		return initArray(n, m, Direction.Down, new Strategy() {
			boolean flag1 = true;
			boolean flag2 = true;
			boolean flag3 = true;
			boolean flag4 = true;

			@Override
			public Direction judgeDirection(int[][] snake, Direction direction, int row, int col) {
				switch (direction) {
					case Right:
						if (!flag4) {
							if (row == 0) {
								direction = Direction.Down;
								flag4 = !flag4;
							}
						} else {
							if (col == m - 1 || snake[row][col + 1] != 0) {
								direction = Direction.Up;
								flag4 = !flag4;
							}
						}
						break;
					case Down:
						if ((row == n - 1) || (snake[row + 1][col] != 0)) {
							if (flag1) {
								direction = Direction.Right;
								flag1 = !flag1;
							} else {
								direction = Direction.Left;
								flag1 = !flag1;
							}
						}
						break;
					case Left:
						if (flag3) {
							if (row == 0) {
								direction = Direction.Down;
								flag3 = !flag3;
							}
						} else {
							if (snake[row][col - 1] != 0) {
								direction = Direction.Up;
								flag3 = !flag3;
							}
						}
						break;
					case Up:
						if (row == 0 || snake[row - 1][col] != 0) {
							if (flag2) {
								direction = Direction.Left;
								flag2 = !flag2;
							} else {
								direction = Direction.Right;
								flag2 = !flag2;
							}
						}
						break;
				}
				return direction;
			}
		});
	}

	public int[][] initArray(int n, int m, Direction direction, Strategy strategy) {
		int[][] snake = new int[n][m];
		int value = 1;
		int row = 0, col = 0;
		for (int i = 0; i < n * m; i++) {
			snake[row][col] = value;
			direction = strategy.judgeDirection(snake, direction, row, col);
			switch (direction) {
				case Right:
					col++;
					break;
				case Down:
					row++;
					break;
				case Left:
					col--;
					break;
				case Up:
					row--;
					break;
				default:
					System.out.println("error");
			}
			value++;
		}
		return snake;
	}

	private interface Strategy {
		Direction judgeDirection(int[][] snake, Direction direction, int row, int col);
	}
}

