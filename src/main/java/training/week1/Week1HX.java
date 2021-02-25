package training.week1;

import org.apache.commons.lang3.tuple.Pair;
import training.week1.hongxing.Node;
import training.week1.hongxing.Snake;
import training.week1.hongxing.Test;

import java.util.Map;

public class Week1HX implements Week1Worker {
	@Override
	public int[][] a(int n, int m) {
		return work(n, m, Test.firstMethod());
	}

	@Override
	public int[][] b(int n, int m) {
		return work(n, m, Test.secondMethod());
	}

	@Override
	public int[][] c(int n, int m) {
		return work(n, m, Test.thirdMethod());
	}

	private static int[][] work(int m, int n, Map<Pair<Node.Direction, Node.Direction>, Node.Direction> strategy) {
		Node<Integer> startNode = new Node<>();
		startNode.setDirection(Node.Direction.right);
		startNode.setData(1);
		startNode.setCoordinate(new Node.Coordinate<>(0, 0));
		Snake simpleSnake = new Snake(startNode, m - 1, n - 1, null, strategy);
		return simpleSnake.toArray();
	}
}
