package training.week11;

import com.wayue.olympus.common.ListUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Week11FZ implements Week11Worker {
	private <T extends BinaryTreeNode<T>> Iterable<Pair<T, Integer>> inOrderTraversal(T root) {
		return () -> new Iterator<Pair<T, Integer>>() {
			private Pair<T, Integer> pair = Pair.of(root, 1);
			private Deque<Pair<T, Integer>> stack = new LinkedList<>();

			@Override
			public boolean hasNext() {
				return pair.getLeft() != null || !stack.isEmpty();
			}

			@Override
			public Pair<T, Integer> next() {
				for (T node = pair.getLeft(); node != null; ) {
					stack.push(pair);
					pair = Pair.of(node = node.getLeft(), pair.getRight() << 1);
				}
				Pair<T, Integer> next = stack.pop();
				pair = Pair.of(next.getLeft().getRight(), next.getRight() << 1 | 1);
				return next;
			}
		};
	}

	private <T extends TreeNode<T>> Iterable<Pair<T, Integer>> preOrderTraversal(T root) {
		Deque<Pair<T, Integer>> stack = new LinkedList<>();
		stack.push(Pair.of(root, 1));
		return () -> new Iterator<Pair<T, Integer>>() {
			@Override
			public boolean hasNext() {
				return !stack.isEmpty();
			}

			@Override
			public Pair<T, Integer> next() {
				Pair<T, Integer> next = stack.pop();
				int i = 0;
				for (T child : ListUtil.reversed(next.getLeft().getChildren())) {
					stack.push(Pair.of(child, next.getRight() << 1 | Boolean.compare(i++ == 0, false)));
				}
				return next;
			}
		};
	}

	@Override
	public <T extends BinaryTreeNode<T>> String prettyPrintBinaryTree(T root) {
		StringBuilder sb = new StringBuilder();
		for (Pair<T, Integer> pair : inOrderTraversal(root)) {
			T node = pair.getLeft();
			int bits = pair.getRight();
			StringBuilder prefix = new StringBuilder();
			Boolean prev = null;
			for (int position = Integer.highestOneBit(bits) >> 1; 0 < position; position >>= 1) {
				boolean curr = (bits & position) != 0;
				if (prev != null) {
					prefix.append(prev == curr ? "     " : "│    ");
				}
				prev = curr;
			}
			boolean lastBit = (bits & 1) == 1, hasNext = node.getRight() != null;
			List<String> lines = node.getTexts();
			for (int i = 0; i < lines.size(); i++) {
				if (1 < bits) {
					sb.append(prefix).append(i == 0 ? lastBit ? "╰───╴" : "╭───╴" : lastBit ? "     " : "│    ");
				}
				sb.append(i == 0 ? '•' : hasNext ? '│' : ' ').append(lines.get(i)).append("\t" + Integer.toBinaryString(bits)).append('\n');
			}
		}
		return sb.toString();
	}

	@Override
	public <T extends TreeNode<T>> String prettyPrintTree(T root) {
		StringBuilder sb = new StringBuilder();
		for (Pair<T, Integer> pair : preOrderTraversal(root)) {
			T node = pair.getLeft();
			int bits = pair.getRight();
			StringBuilder prefix = new StringBuilder();
			for (int position = Integer.highestOneBit(bits) >> 1; 1 < position; position >>= 1) {
				boolean curr = (bits & position) != 0;
				prefix.append(curr ? "     " : "│    ");
			}
			boolean lastBit = (bits & 1) == 1, hasNext = !node.getChildren().isEmpty();
			List<String> lines = node.getTexts();
			for (int i = 0; i < lines.size(); i++) {
				if (1 < bits) {
					sb.append(prefix).append(i == 0 ? lastBit ? "╰───╴" : "├───╴" : lastBit ? "     " : "│    ");
				}
				sb.append(i == 0 ? '•' : hasNext ? '│' : ' ').append(lines.get(i)).append("\t" + Integer.toBinaryString(bits)).append('\n');
			}
		}
		return sb.toString();
	}
}
