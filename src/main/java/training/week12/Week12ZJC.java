package training.week12;


import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Iterator;

public class Week12ZJC implements Week12Worker {
    @Override
    public Iterable<String> parse(Iterable<String> lines) {
        Iterator<String> iterator = lines.iterator();
        return () -> new Iterator<String>(){
            int lev;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                String next = iterator.next();
//                int count = StringUtils.countOccurrencesOf(next, indent);
                int count = StringUtils.countMatches(next, indent);
                lev = lev + (1 << count * 4);
                lev &= (0xffffffff >>> (32 - 4 * count -4));
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(StringUtils.repeat(indent,count));
                for (int i = lev; i > 0; i >>= 4){
                    stringBuffer.append(i & 0xf).append(".");
                }
                return stringBuffer.append(next.replaceAll(indent, StringUtils.EMPTY)).toString();
            }
        };
    }
}
