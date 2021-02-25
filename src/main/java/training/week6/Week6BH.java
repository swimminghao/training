package training.week6;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/9/6
 */
public class Week6BH implements Week6Worker{

    @Override
    public List<Pair<String, Integer>> work(List<Pair<String, String>> personToPaper, List<Pair<String, String>> paperToReference) {

        List<Pair<String, Integer>> result = new ArrayList<>();

        //论文 -> 发表人映射 方便查找引用的论文是否在统计范围
        Map<String,String> paperPerson = new HashMap<>();
        for (Pair<String, String> item : personToPaper) {
            paperPerson.put(item.getRight(),item.getLeft());
        }

        //发表人 -> 论文 -> 次数映射, 计算H指数所需数据
        Map<String,Map<String,Integer>> personPaperCount = new HashMap<>();
        for (Pair<String, String> item : paperToReference) {
            String paper = item.getRight();
            String person = paperPerson.get(paper);
            if (!StringUtils.isEmpty(person)) {
                Map<String, Integer> paperCount = personPaperCount.get(person);
                if (null == paperCount) {
                    paperCount = new HashMap<>();
                    paperCount.put(paper,1);
                    personPaperCount.put(person,paperCount);
                } else {
                    paperCount.merge(paper, 1, (a, b) -> a + b);
                }
            }
        }

        //计算H指数
        for (Map.Entry<String, Map<String, Integer>> item : personPaperCount.entrySet()) {
            String person = item.getKey();
            List<Integer> paperCountList = new ArrayList<>(item.getValue().values());
            Collections.sort(paperCountList);
            int h = 0;
            for (int i = 0; i < paperCountList.size(); i++) {
                int currH = Math.min(paperCountList.get(i), paperCountList.size() - i);
                if(currH > h){
                    h = currH;
                }
            }
            result.add(Pair.of(person,h));
        }

        return result;
    }
}
