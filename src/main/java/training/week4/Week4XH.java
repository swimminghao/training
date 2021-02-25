package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Week4XH implements Week4Worker {
    @Override
    public <T> List<Collection<T>> sort(List<Pair<T, T>> relations) {


        Map<T, List<T>> map = new HashMap<>();

        for (int i = 0; i < relations.size(); i++) {
            Pair<T, T> pair = relations.get(i);
            T left = pair.getLeft();
            map.computeIfAbsent(pair.getRight(), key -> new ArrayList<T>()).add(left);
        }
        Set<T> uniqueValues = new HashSet<>();

        for (Pair<T, T> relation : relations) {
            T k = relation.getLeft();
            if (uniqueValues.add(k)) {
                map.putIfAbsent(k, new ArrayList<>());
            }
        }
        List<Collection<T>> result = new ArrayList<>();

        while (!map.isEmpty()){
            List<T> del = new ArrayList<>();
            for (Map.Entry<T, List<T>> e : map.entrySet()) {
                if (e.getValue().isEmpty()) {
                    T key = e.getKey();
                    del.add(key);
                }
            }
            del.forEach(t -> {
                map.values().forEach(c -> c.remove(t));
                map.remove(t);
            });
            result.add(del);
        }

        return result;
    }
}
