package training.week1.hongxing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node<T> {
	private T data;
	private Coordinate<Integer> coordinate;
	private Direction direction;
	private Node<T> previous;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Coordinate<D extends Number> {
		private D x;
		private D y;
	}

	public enum Direction {
		up("⬆"), down("⬇"), right("➡"), left("⬅");
		private String val;

		Direction(String val) {
			this.val = val;
		}

		public String getVal() {
			return this.val;
		}
	}

	public Node.Coordinate<Integer> getNextCoordinate() {
		Node.Direction direction = this.getDirection();
		Node.Coordinate<Integer> coordinate = this.getCoordinate();
		Node.Coordinate<Integer> nextCoordinate = new Node.Coordinate<>();
		switch (direction) {
			case left:
				nextCoordinate.setX(coordinate.getX() - 1);
				nextCoordinate.setY(coordinate.getY());
				break;
			case right:
				nextCoordinate.setX(coordinate.getX() + 1);
				nextCoordinate.setY(coordinate.getY());
				break;
			case up:
				nextCoordinate.setX(coordinate.getX());
				nextCoordinate.setY(coordinate.getY() - 1);
				break;
			case down:
				nextCoordinate.setX(coordinate.getX());
				nextCoordinate.setY(coordinate.getY() + 1);
				break;
		}
		return nextCoordinate;
	}
}
