package training.week9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WeekZJC implements Week9Worker {
    @Override
    public <T> Heap<T> getHeap(Comparator<T> comparator) {
        return new Heap<T>() {
            private List<T> list = new ArrayList();

            @Override
            public void offer(T t) {
                list.add(t);
                int son =list.size() - 1;
                for (; son != 0; son = (son - 1)/2){
                    int parent = (son - 1)/2;
                    if (comparator.compare((T) list.get(parent), t) <= 0){
                        break;
                    }else{
                        list.set(son, list.get(parent));
                        list.set(parent, t);
                    }
                }
            }

            @Override
            public T poll() {
                if (list.isEmpty()){
                    return null;
                }else if (list.size() == 1){
                    return (T)list.remove(0);
                }else {
                    T top = list.set(0, list.remove(list.size() - 1));
                    T newTop = list.get(0);
                    for (int p = 0; ; ){
                        int left = 2*p+1; int right = 2*p+2;
                        if (left >= list.size()){
                            break;
                        }else if (right >= list.size()){
                            if (comparator.compare(newTop, list.get(left)) > 0){
                                list.set(p, list.get(left));
                                list.set(left, newTop);
                            }
                            break;
                        }else {
                            int min = comparator.compare(list.get(left), list.get(right)) < 0 ? left : right;
                            if (comparator.compare(newTop, list.get(min)) > 0){
                                list.set(p, list.get(min));
                                list.set(min, newTop);
                                p = min;
                            }else {
                                break;
                            }
                        }
                    }
                    return top;
                }
            }
        };
    }
}
