package training.week11;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RunWith(Parameterized.class)
public class Week11WorkerTest {
	private Week11Worker worker;
	private static Node root;

	@Getter
	@RequiredArgsConstructor
	public static class Node implements Week11Worker.BinaryTreeNode<Node> {
		public Node left, right;
		public final String text;

		@Override
		public List<String> getTexts() {
			return Arrays.asList(text, "1.xxx", "2.xx");
		}
	}

	static {
		root = new Node("a");
		Queue<Node> queue = new LinkedList<>();
		for (queue.offer(root); !queue.isEmpty(); ) {
			Node node = queue.remove();
			if (0 < 2 + 3 * Math.random() - node.text.length()) {
				node.left = new Node(node.text + "a");
			}
			if (0 < 2 + 3 * Math.random() - node.text.length()) {
				node.right = new Node(node.text + "b");
			}
			node.getChildren().forEach(queue::offer);
		}
	}

	public Week11WorkerTest(Class<? extends Week11Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week11FZ.class },
//				{ Week11GBH.class },
//				{ Week11GJC.class },
//				{ Week11GJC2.class },
//				{ Week11HX.class },
//				{ Week11ZQ.class },
				};
	}

	@Test
	public void test() {
		System.out.println("prettyPrintBinaryTree");
		System.out.println(worker.prettyPrintBinaryTree(root));
		System.out.println("prettyPrintTree");
		System.out.println(worker.prettyPrintTree(root));
	}
}