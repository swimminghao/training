package training.week10;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.function.UnaryOperator;

@RunWith(Parameterized.class)
public class Week10WorkerTest {
	private Week10Worker worker;

	public Week10WorkerTest(Class<? extends Week10Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week10ZQ.class },
				};
	}

	@Test
	public void test() {
		for (int n = 1; n < 6; n++) {
			int[] BST = worker.BSTGenerator(n);
			System.out.println("-----\nn = " + n);
			for (int i : BST) {
				System.out.println(i);
			}
			Assert.assertEquals(1 << n, BST.length + 1);
			UnaryOperator<Integer> left = i -> i < BST.length / 2 ? i * 2 + 1 : null;
			UnaryOperator<Integer> right = i -> i < BST.length / 2 ? i * 2 + 2 : null;
			Integer prev = null;
			for (Integer i : worker.BSTIterator(0, left, right)) {
				int current = BST[i];
				if (prev != null) {
					Assert.assertTrue(prev < current);
				}
				prev = current;
			}
		}
	}
}