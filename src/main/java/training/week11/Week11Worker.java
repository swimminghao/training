package training.week11;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Week11Worker {
	interface BinaryTreeNode<T extends TreeNode<T>> extends TreeNode<T> {
		@Override
		default List<T> getChildren() {
			return Stream.of(getLeft(), getRight()).filter(Objects::nonNull).collect(Collectors.toList());
		}

		T getLeft();

		T getRight();
	}

	interface TreeNode<T extends TreeNode<T>> {
		List<T> getChildren();

		List<String> getTexts();
	}

	<T extends BinaryTreeNode<T>> String prettyPrintBinaryTree(T root);

	<T extends TreeNode<T>> String prettyPrintTree(T root);
}
