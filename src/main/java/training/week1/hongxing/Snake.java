package training.week1.hongxing;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class Snake {
	private Node<Integer> start;
	private Node<Integer> currentNode;
	private Map<Node.Coordinate, Node<Integer>> nodes;
	private int rightLimit;
	private int bottomLimit;
	private Map<Pair<Node.Direction, Node.Direction>, Node.Direction> meetBoundStrategy;
	private Map<Pair<Node.Direction, Node.Direction>, Node.Direction> meetNothingStrategy;

	public Snake(Node<Integer> start, int rightLimit, int bottomLimit, Map<Pair<Node.Direction, Node.Direction>, Node.Direction> meetBoundStrategy, Map<Pair<Node.Direction, Node.Direction>, Node.Direction> meetNothingStrategy) {
		this.meetBoundStrategy = meetBoundStrategy;
		this.meetNothingStrategy = meetNothingStrategy;
		this.start = start;
		this.currentNode = start;
		this.rightLimit = rightLimit;
		this.bottomLimit = bottomLimit;
	}

	public int[][] toArray() {
		generateNodes();
		int[][] array = new int[bottomLimit + 1][];
		for (int j = 0; j <= bottomLimit; j++) {
			array[j] = new int[rightLimit + 1];
			for (int i = 0; i <= rightLimit; i++) {
				array[j][i] = nodes.get(new Node.Coordinate<>(i, j)).getData();
			}
		}
		return array;
	}

	public List<List<Node<Integer>>> toList() {
		generateNodes();
		List<List<Node<Integer>>> list = new ArrayList<>();
		for (int j = 0; j <= bottomLimit; j++) {
			List<Node<Integer>> row = new ArrayList<>();
			for (int i = 0; i <= rightLimit; i++) {
				row.add(nodes.get(new Node.Coordinate<>(i, j)));
			}
			list.add(row);
		}
		return list;
	}

	public String prettyPrint(Function<Node<Integer>, String> mapper) {
		List<List<Node<Integer>>> lists = toList();
		return lists.stream().map(snakeNodes -> snakeNodes.stream().map(s -> {
			if (s == null) {
				return "";
			} else {
				return mapper.apply(s);
			}
		}).collect(Collectors.joining("\t"))).collect(Collectors.joining("\r\n"));
	}

	private Node.Direction whenMeetBound(Node.Direction previous, Node.Direction direction) {
		return meetBoundStrategy.get(Pair.of(previous, direction));
	}

	private Node.Direction whenMeetNothing(Node.Direction previous, Node.Direction direction) {
		return meetNothingStrategy.get(Pair.of(previous, direction));
	}

	private void generateNodes() {
		if (nodes == null) {
			nodes = new LinkedHashMap<>();
			nodes.put(start.getCoordinate(), start);
			for (; getNextNode(); ) {
				getNextNode();
			}
		}
	}

	private boolean isFull() {
		return nodes.size() == (rightLimit + 1) * (bottomLimit + 1);
	}

	private boolean getNextNode() {
		if (isFull()) {
			return false;
		}
		Node.Direction newDir;
		boolean meetBound = false;
		List<Node.Direction> availableDirections = getAvailableDirections(currentNode);
		if (availableDirections.isEmpty()) {
			throw new RuntimeException("no direction can expand for node : " + currentNode.getCoordinate());
		} else if (availableDirections.size() == 1) {
			newDir = availableDirections.get(0);
		} else {
			if (currentNode.getPrevious() != null) {
				if (meetBound(currentNode.getNextCoordinate())) {
					newDir = whenMeetBound(currentNode.getPrevious().getDirection(), currentNode.getDirection());
					meetBound = true;
				} else {
					newDir = whenMeetNothing(currentNode.getPrevious().getDirection(), currentNode.getDirection());
				}
			} else {
				newDir = currentNode.getDirection();
			}
			if (!availableDirections.contains(newDir)) {
				throw new RuntimeException(String.format("direction [%s] is not in %s while meetBound : [%s] from : [%s]  -> [%s]", newDir, availableDirections, meetBound, currentNode.getPrevious().getDirection(), currentNode.getDirection()));
			}
		}
		if (newDir != null) {
			Node<Integer> nextNode = new Node<>(currentNode.getData() + 1, currentNode.getCoordinate(), newDir, currentNode);
			Node.Coordinate<Integer> nextCoordinate = nextNode.getNextCoordinate();
			nextNode.setCoordinate(nextCoordinate);
			if (!meetBound(nextNode.getCoordinate())) {
				nodes.put(nextCoordinate, nextNode);
				currentNode = nextNode;
				return true;
			}
		} else {
			throw new RuntimeException(String.format("cannot get next node direction while meetBound : [%s] from : [%s]  -> [%s]", meetBound, currentNode.getPrevious().getDirection(), currentNode.getDirection()));
		}
		return false;
	}

	private boolean meetBound(Node.Coordinate<Integer> coordinate) {
		if (coordinate.getX() < 0 || coordinate.getY() < 0 || coordinate.getX() > rightLimit || coordinate.getY() > bottomLimit) {
			return true;
		} else {
			return nodes.get(coordinate) != null;
		}
	}

	private List<Node.Direction> getAvailableDirections(Node<Integer> node) {
		Node.Direction originDirection = node.getDirection();
		List<Node.Direction> list = new ArrayList<>();
		for (Node.Direction value : Node.Direction.values()) {
			node.setDirection(value);
			if (!meetBound(node.getNextCoordinate())) {
				list.add(value);
			}
		}
		node.setDirection(originDirection);
		return list;
	}
}
