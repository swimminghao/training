package training.week12;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import training.week11.Week11Worker;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Week12XH implements Week12Worker {
    @Override
    public Iterable<String> parse(Iterable<String> lines) {
        Iterator<String> iterator = lines.iterator();
        return () -> new Iterator<String>() {
            int bits = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                String next = iterator.next();
                //n为空格数
                int n = 0;
                for (int i = 0; i < next.length(); i++) {
                    if (next.charAt(i) != ' ') {
                        n = i;
                        break;
                    }
                }
                {
                    bits += 1 << (n / 2 * 4);
//                      if (n==0) bits = bits & 0b1111;
//                      bits = bits & ((1<<((n/2+1) *4 +1))-1);
                    bits &= (-1) >>> (32 - (n / 2 + 1) * 4);
                }
                StringBuilder sb = new StringBuilder();
                for (int b = bits; 0 < b; b >>= 4) {
                    sb.append(b & 0b1111).append('.');
                }
                return StringUtils.repeat(' ', n) + sb + next.trim();
            }
        };
    }
}