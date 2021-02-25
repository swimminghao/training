package training.week11;

import com.wayue.olympus.common.ListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Week11HX implements Week11Worker {
	@Override
	public <T extends BinaryTreeNode<T>> String prettyPrintBinaryTree(T root) {
		StringBuilder sb = new StringBuilder();
		for (Pair<T, Integer> pair : midOrder(root)) {
			Integer value = pair.getRight();
			List<String> texts = pair.getLeft().getTexts();
			for (int i = 0; i < texts.size(); i++) {
				for (int j = Integer.SIZE - Integer.numberOfLeadingZeros(value) - 2; j >= 0; j--) {
					boolean isRight = (value & (1 << j)) != 0;
					if (i == 0 && j == 1) {
						sb.append(isRight ? "╰───╴" : "╭───╴");
					} else if (j != 0) {
						boolean nextIsRight = (value & (1 << (j - 1))) != 0;
						sb.append((j == 1 && isRight) || (j != 1 && isRight == nextIsRight) ? " " : "│").append("    ");
					} else {
						sb.append(i == 0 ? "•" : isRight ? "|" : " ");
					}
				}
				sb.append(texts.get(i)).append("\n");
			}
		}
		return sb.toString();
	}

	@Override
	public <T extends TreeNode<T>> String prettyPrintTree(T root) {
		StringBuilder sb = new StringBuilder();
		for (Pair<T, Integer> pair : preOrder(root)) {
			Integer value = pair.getRight();
			List<String> texts = pair.getLeft().getTexts();
			for (int i = 0; i < texts.size(); i++) {
				for (int j = Integer.SIZE - Integer.numberOfLeadingZeros(value) - 2; j >= 0; j--) {
					boolean hasNeighbour = (value & (1 << j)) != 0;
					if (i == 0 && j == 1) {
						sb.append(hasNeighbour ? "├───╴" : "╰───╴");
					} else if (j != 0) {
						sb.append(hasNeighbour ? "│    " : "     ");
					} else {
						sb.append(i == 0 ? "•" : hasNeighbour ? "|" : " ");
					}
				}
				sb.append(texts.get(i)).append("\n");
			}
		}
		return sb.toString();
	}

	private static <T extends TreeNode<T>> Iterable<Pair<T, Integer>> preOrder(T root) {
		Deque<Pair<T, Integer>> stack = new LinkedList<>();
		stack.push(Pair.of(root, CollectionUtils.isNotEmpty(root.getChildren()) ? 3 : 2));
		return () -> new Iterator<Pair<T, Integer>>() {
			@Override
			public boolean hasNext() {
				return !stack.isEmpty();
			}

			@Override
			public Pair<T, Integer> next() {
				Pair<T, Integer> next = stack.pop();
				boolean first = true;
				for (T child : ListUtil.reversed(next.getLeft().getChildren())) {
					int value = CollectionUtils.isNotEmpty(child.getChildren()) ? next.getRight() << 1 | 1 : next.getRight() << 1;
					stack.push(Pair.of(child, first ? value & ~2 : value | 2));
					first = false;
				}
				return next;
			}
		};
	}

	private static <T extends BinaryTreeNode<T>> Iterable<Pair<T, Integer>> midOrder(T root) {
		Deque<Pair<T, Integer>> stack = new LinkedList<>();
		return () -> new Iterator<Pair<T, Integer>>() {
			Pair<T, Integer> node = Pair.of(root, root.getRight() != null ? 3 : 2);

			@Override
			public boolean hasNext() {
				return node != null || !stack.isEmpty();
			}

			@Override
			public Pair<T, Integer> next() {
				for (; node != null; ) {
					stack.push(node);
					T leftChild = node.getLeft().getLeft();
					node = leftChild == null ? null : Pair.of(leftChild, leftChild.getRight() != null ? (node.getRight() << 1 | 1) & ~2 : node.getRight() << 1 & ~2);
				}
				Pair<T, Integer> next = stack.pop();
				T rightChild = next.getLeft().getRight();
				node = rightChild == null ? null : Pair.of(rightChild, rightChild.getRight() != null ? next.getRight() << 1 | 3 : next.getRight() << 1 | 2);
				return next;
			}
		};
	}
}
