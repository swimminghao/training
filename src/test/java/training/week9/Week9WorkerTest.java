package training.week9;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import training.week9.Week9Worker.Heap;

import java.util.Comparator;
import java.util.PriorityQueue;

@RunWith(Parameterized.class)
public class Week9WorkerTest {
	private Week9Worker worker;

	public Week9WorkerTest(Class<? extends Week9Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week9HX.class },
				{ Week9FZ.class },
				};
	}

	@Test
	public void test() {
		test(Integer::compareTo);
		test(((Comparator<Integer>) Integer::compare).reversed());
	}

	private void test(Comparator<Integer> comparator) {
		Heap<Integer> heap = worker.getHeap(comparator);
		PriorityQueue<Integer> queue = new PriorityQueue<>(comparator);
		for (float r = 0; r < 1; r += 0.1) {
			for (int i = 0; i < 1000; i++) {
				if (Math.random() < r) {
					int t = (int) (Math.random() * 50);
					heap.offer(t);
					queue.offer(t);
				} else {
					Assert.assertEquals(queue.poll(), heap.poll());
				}
			}
			for (; !queue.isEmpty(); ) {
				Assert.assertEquals(queue.poll(), heap.poll());
			}
			Assert.assertNull(heap.poll());
		}
	}
}