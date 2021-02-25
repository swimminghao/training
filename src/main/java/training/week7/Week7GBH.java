package training.week7;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/9/22
 */
public class Week7GBH implements Week7Worker{

    @Override
    public int[] intersections(int[] unions) {

        int [] result = new int[unions.length];

        for (int i = 1; i < unions.length; i++) {
            for (int j = 1; j < unions.length; j++) {
                if ((j & i) == j) {
                    result[i] += ((Integer.bitCount(j) & 1) * 2 - 1 ) * unions[j];
                }
            }
        }

        return result;
    }

    @Override
    public int[] partitions(int[] unions) {
        return new int[0];
    }
}
