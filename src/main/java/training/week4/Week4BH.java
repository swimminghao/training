package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/8/15
 */
public class Week4BH implements Week4Worker{

    @Override
    public <T> List<Collection<T>> sort(List<Pair<T, T>> relations) {
        List<Collection<T>> result = new ArrayList<>();

        Map<T,List<T>> post = new HashMap<>();

        for (Pair<T, T> item : relations) {
            List<T> list = post.get(item.getRight());
            if (null == list) {
                list = new ArrayList<>();
            }
            list.add(item.getLeft());
            post.put(item.getRight(),list);
        }

        Set<T> set = new HashSet<>();
        for (Pair<T, T> relation : relations) {
            set.add(relation.getLeft());
            set.add(relation.getRight());
        }

        List<T> pre = new ArrayList<>();
        for (T item : set) {
            if (null == post.get(item)) {
                pre.add(item);
            }
        }
        result.add(pre);

        Set<T> already = new HashSet<>();
        already.addAll(pre);
        Set<Map.Entry<T, List<T>>> entries = post.entrySet();

        while (!post.isEmpty()) {
            Set<T> temp = new HashSet<>();
            for (Map.Entry<T, List<T>> entry : entries) {
                List<T> value = entry.getValue();
                if (already.containsAll(value)) {
                    temp.add(entry.getKey());
                }
            }
            for (T item : temp) {
                post.remove(item);
            }
            already.addAll(temp);
            result.add(temp);
        }

        return result;
    }
}
