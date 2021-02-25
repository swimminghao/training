package training.week12;

import java.util.Iterator;

/**
 * Copyright (C), 2018-2019 善诊
 * Week12ZQ
 *
 * @Author: zhuqiang
 * @Date: 2019-11-27
 */
public class Week12ZQ implements Week12Worker {
    private int hexOffset = 4;

    @Override
    public Iterable<String> parse(Iterable<String> lines) {

        Iterator<String> iterator = lines.iterator();
        return () -> new Iterator<String>() {
            private Integer tag = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                StringBuilder stringBuilder = new StringBuilder();
                String next = iterator.next();
                int firstNotSpaceLocation =
                        next.indexOf(next.chars().filter(c -> c != 32).findFirst().getAsInt());
                stringBuilder.append(next.substring(0, firstNotSpaceLocation));
                int level = firstNotSpaceLocation / indent.length();
                int upperLevel = (Integer.SIZE - Integer.numberOfLeadingZeros(tag)) / (hexOffset+1);
                tag = upperLevel > level ? tag >> (upperLevel - level)*hexOffset : tag << (level - upperLevel)*hexOffset;
                tag = tag + 1;
                for (Character c :
                        Integer.toHexString(tag).toCharArray()) {
                    stringBuilder.append(Integer.parseInt(c.toString(), 16)).append(".");
                }
                stringBuilder.append(next.substring(firstNotSpaceLocation));
                return stringBuilder.toString();
            }
        };


    }
}
