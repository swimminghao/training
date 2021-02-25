package training.week10;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

public class Week10HZY implements Week10Worker {
	@Override
	 public int[] BSTGenerator(int n){
		//n = 1
		if (n == 1){
			return new int[] { n };
		}
		//n > 1
		int[] BST = new int[(1 << n) - 1];
		int root = 2 << n - 2;
		BST[0] = root;
		//i是层数，j为索引
		for (int i = 1, j = 1; i < n; i++, root = 1<< n - i) {
			int length = 1 << n - i - 1, num = 1 << i;
			root = root / 2;
			//k控制每层个数
			for (int k = 1; k <= num; k++, root = root + 2 * length){
				BST[j++] = root;
			}
		}
		return BST;
	}

	@Override
	public <T> Iterable<T> BSTIterator(T root, UnaryOperator<T> left, UnaryOperator<T> right) {
		return () -> new Iterator<T>() {
			Deque<T> deque = new LinkedList<>();
			{
				deque.push(root);
				T l = left.apply(root);
				while (l != null){
					deque.push(l);
					l = left.apply(l);
				}
			}
			@Override
			public boolean hasNext() {
				if (deque.isEmpty()){
					return false;
				}
				return true;
			}
			@Override
			public T next() {
				T min = deque.poll();
				T r = right.apply(min);
				if (r != null){
					deque.push(r);
					T l = left.apply(r);
					while (l != null){
						deque.push(l);
						l = left.apply(l);
					}
				}
				return min;
			}
		}
		;
	}
}
