package training.week12;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Week12GJC implements Week12Worker {

    @Override
    public Iterable<String> parse(Iterable<String> lines) {
        Iterator<String> lineIterator = lines.iterator();
        return () -> new Iterator<String>() {
            int indentIndex = 0;

            @Override
            public boolean hasNext() {
                return lineIterator.hasNext();
            }

            @Override
            public String next() {
                String line = lineIterator.next();
                int indentNum = StringUtils.countMatches(line, indent) + 1;
                indentIndex = indentIndex(indentNum, indentIndex);
                String[] ss = Integer.toString(indentIndex, 16).split("");
                StringBuilder sb = new StringBuilder(StringUtils.repeat(indent, indentNum - 1));
                Arrays.stream(ss).forEach(s -> sb.append(s).append("."));
                sb.append(line.replace(indent, ""));
                return sb.toString();
            }
        };
    }

    //获取缩进编号by当前需要缩进+上一个缩进编号
    private static int indentIndex(int indentNum, int lastIndex) {
        int currentIndex = lastIndex;
        int lastIndentNum = 8 - Integer.numberOfLeadingZeros(lastIndex) / 4; //16进制下的位数
        if (indentNum > lastIndentNum) {
            currentIndex <<= 4;
        } else if (indentNum < lastIndentNum) {
            for (int i = 0; i < lastIndentNum - indentNum; i++) {
                currentIndex >>= 4;
            }
        }
        return ++currentIndex;
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("XXX", "  XXX", "  XXX", "    XXX", "  XXX", "XXX");
        list.forEach(System.out::println);

        Week12GJC w = new Week12GJC();
        Iterable<String> list2 = w.parse(list);
        list2.forEach(System.out::println);


    }
}
