package training.week9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Week9HZY implements Week9Worker {
	@Override
	public <T> Heap <T> getHeap(Comparator<T> comparator){ return new Heap<T>() {
			private List<T> array = new ArrayList<>();

			//插入
			@Override
			public void offer(T t) {
				array.add(t);
				int hole = array.size();
				for (; hole - 1 != 0; hole = (hole - 2) / 2 + 1) {
					if (comparator.compare(t, array.get((hole - 2) / 2)) < 0) {
						array.set(hole-1, array.set((hole - 2) / 2, t));
					}else {
						break;
					}
				}
			}
			//删除最小值
			@Override
			public T poll() {
				if (array.isEmpty()) {
					return null;
				}
				if (array.size() == 1){
					return array.remove(0);
				}
				T min = array.get(0);
				array.set(0, array.remove(array.size()-1));
				parmePutDown(0);
				return min;
			}

			//取出最小值后，再排序
			private void parmePutDown(int hole) {
				int child;
				T temp = array.get(hole);
				for (; hole * 2 + 1 < array.size(); hole = child) {
					child = hole * 2 + 1;
					if (child != array.size()-1 && comparator.compare(array.get(child + 1), array.get(child)) < 0) {
						child++;
					}
					if (comparator.compare(array.get(child), temp) < 0) {
						array.set(hole, array.get(child));
					} else
						break;
				}
				array.set(hole, temp);
			}

		};
	}
}
