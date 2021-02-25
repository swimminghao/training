package training.week7;

import com.alibaba.fastjson.JSON;

public class Week7GJC implements Week7Worker {
    @Override
    public int[] intersections(int[] unions) {
        int[] intersections = new int[unions.length];
        for (int i = 1; i < unions.length; i++) {
            for (int j = 1; j < unions.length; j++) {
                if ((j & i) == j) {
                    intersections[i] += unions[j] * (isOddOne(j) ? 1 : -1);
                }
            }
        }
        return intersections;
    }

    @Override
    public int[] partitions(int[] unions) {
        /*int[] partitions = new int[unions.length];
        int[] intersections = intersections(unions);
        for (int i = 1; i < unions.length; i++) {
            for (int j = 1; j < unions.length; j++) {
                if ((j & i) == j) {
                    partitions[i] += unions[j] * (isOddOne(j) ? 1 : -1);
                }
            }
        }*/
        return null;
    }

    private static boolean isOddOne(int i) {
        return Integer.bitCount(i) % 2 == 1;
    }

    public static void main(String[] args) {
        Week7GJC worker = new Week7GJC();

        int[] union = {0, 5060, 5008, 7544, 4930, 7465, 7445, 8752};
        System.out.println(JSON.toJSONString(union));
        int[] out = worker.intersections(union);
        System.out.println(JSON.toJSONString(out));
    }
}
