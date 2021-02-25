package training.week1.hongxing;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	private static Map<Pair<Node.Direction, Node.Direction>, Node.Direction> common() {
		Map<Pair<Node.Direction, Node.Direction>, Node.Direction> strategy = new HashMap<>();
		strategy.put(Pair.of(Node.Direction.right, Node.Direction.right), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.left), Node.Direction.left);
		strategy.put(Pair.of(Node.Direction.up, Node.Direction.up), Node.Direction.up);
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.down), Node.Direction.down);
		return strategy;
	}

	public static Map<Pair<Node.Direction, Node.Direction>, Node.Direction> firstMethod() {
		Map<Pair<Node.Direction, Node.Direction>, Node.Direction> strategy = common();
		strategy.put(Pair.of(Node.Direction.right, Node.Direction.down), Node.Direction.left);
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.right), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.down), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.left), Node.Direction.left);
		return strategy;
	}

	public static Map<Pair<Node.Direction, Node.Direction>, Node.Direction> secondMethod() {
		Map<Pair<Node.Direction, Node.Direction>, Node.Direction> strategy = common();
		strategy.put(Pair.of(Node.Direction.up, Node.Direction.right), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.right, Node.Direction.down), Node.Direction.down);
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.left), Node.Direction.left);
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.up), Node.Direction.up);
		return strategy;
	}

	public static Map<Pair<Node.Direction, Node.Direction>, Node.Direction> thirdMethod() {
		Map<Pair<Node.Direction, Node.Direction>, Node.Direction> strategy = common();
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.left), Node.Direction.left);
		strategy.put(Pair.of(Node.Direction.right, Node.Direction.up), Node.Direction.up);
		strategy.put(Pair.of(Node.Direction.right, Node.Direction.down), Node.Direction.down);
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.up), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.down), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.up, Node.Direction.left), Node.Direction.left);
		strategy.put(Pair.of(Node.Direction.up, Node.Direction.right), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.left), Node.Direction.left);
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.right), Node.Direction.right);
		return strategy;
	}

	public static Map<Pair<Node.Direction, Node.Direction>, Node.Direction> fourthMethod() {
		Map<Pair<Node.Direction, Node.Direction>, Node.Direction> strategy = common();
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.left), Node.Direction.up);
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.down), Node.Direction.right);
		strategy.put(Pair.of(Node.Direction.right, Node.Direction.down), Node.Direction.down);
		strategy.put(Pair.of(Node.Direction.left, Node.Direction.up), Node.Direction.up);
		strategy.put(Pair.of(Node.Direction.up, Node.Direction.left), Node.Direction.left);
		strategy.put(Pair.of(Node.Direction.down, Node.Direction.right), Node.Direction.right);
		return strategy;
	}

	public static void main(String[] args) {
		Node<Integer> startNode = new Node<>();
		startNode.setDirection(Node.Direction.right);
		startNode.setPrevious(null);
		startNode.setData(1);
		startNode.setCoordinate(new Node.Coordinate<>(0, 0));
//		firstMethod();
//		secondMethod();
//		thirdMethod();
		Snake simpleSnake = new Snake(startNode, 5, 5, null, thirdMethod());
		int[][] ints = simpleSnake.toArray();
		System.out.println(simpleSnake.prettyPrint(s -> s.getData().toString()));
		System.out.println(simpleSnake.prettyPrint(s -> s.getDirection().getVal()));
	}
}
