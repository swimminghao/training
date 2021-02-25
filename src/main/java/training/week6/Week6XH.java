package training.week6;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xh
 * @date 2019/9/4
 * @description week6作业_论文h值
 */
public class Week6XH implements Week6Worker {
    /**
     *
     * @param personToPaper    List&lt;Person, Paper&gt;
     * @param paperToReference List&lt;Paper, Referenced-Paper&gt;
     * @return Person->h值的list
     */
    @Override
    public List<Pair<String, Integer>> work(List<Pair<String, String>> personToPaper, List<Pair<String, String>> paperToReference) {
        //初始化论文引用：论文->引用数目
        Map<String, Integer> paperReferencedCountMap = new HashMap<>();
        for (Map.Entry<String, List<Pair<String, String>>> stringListEntry : paperToReference.stream()
                .collect(Collectors.groupingBy(Pair::getRight)).entrySet()) {
            if (paperReferencedCountMap.put(stringListEntry.getKey(), stringListEntry.getValue().size()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        //生成列表：person->h值
        List<Pair<String, Integer>> list = new ArrayList<>();
        for (Map.Entry<String, List<Pair<String, String>>> e : personToPaper.stream()
                .collect(Collectors.groupingBy(Pair::getLeft)).entrySet()) {
            //获取每人各个论文的引用数的Integer数组
            Integer[] integers = e.getValue().stream().map(p -> paperReferencedCountMap.getOrDefault(p.getRight(), 0)).toArray(Integer[]::new);
            Pair<String, Integer> of = Pair.of(e.getKey(), hIndex(integers));
            list.add(of);
        }
        return list;
    }

    /**
     *
     * @param citations
     * @return 一个人论文的h值
     * @description 计算一个人论文的h值
     */
    public int hIndex(Integer[] citations) {
        Arrays.sort(citations);
        int n = citations.length;
        if (n == 0) return 0;
        int min = 0, max = citations.length - 1;
        while (min <= max) {
            int mid = (min + max) / 2;
            // 如果该点是有效的H指数，则最大H指数一定在右边
            if (citations[mid] < n - mid) {
                min = mid + 1;
                // 否则最大H指数在左边
            } else {
                max = mid - 1;
            }
        }
        // n - min是min点的H指数
        return n - min;
    }
}
