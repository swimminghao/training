package training.week12;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/12/1
 */
public class Week12GBH implements Week12Worker{

    @Override
    public Iterable<String> parse(Iterable<String> lines) {
        Iterator<String> iterator = lines.iterator();
        return () -> new Iterator<String>() {
            int allIndents = 0;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                String next = iterator.next();
                StringBuilder line = new StringBuilder();
                int level = indents(next);
                allIndents = (((1 << (level * 4)) - 1 ) & allIndents) + (1 << ((level - 1) * 4));
                int temp = allIndents;
                for (int i = 0; i < level - 1; i++) {
                    line.append(indent);
                }
                for (int i = 0; i < level; i++) {
                    int current = temp & 0b1111;
                    line.append(String.valueOf(current)).append(".");
                    temp = temp >> 4;
                }
                line.append(next.trim());
                return line.toString();
            }
        };
    }

    private int indents(String str) {
        int indents = 1;
        char[] source = indent.toCharArray();
        char[] target = str.toCharArray();

        label:
        for (int i = 0; i < target.length; i = i + source.length) {
            if (i + source.length > target.length) {
                break;
            }
            char[] chars = Arrays.copyOfRange(target, i, i + source.length);
            for (int i1 = 0; i1 < source.length; i1++) {
                if (source[i1] != chars[i1]) {
                    break label;
                }
            }
            indents++;
        }

        return indents;
    }
}
