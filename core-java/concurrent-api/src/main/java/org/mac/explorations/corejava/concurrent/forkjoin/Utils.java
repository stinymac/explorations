/*
 *          (          (
 *          )\ )  (    )\   )  )     (
 *  (  (   (()/( ))\( ((_| /( /((   ))\
 *  )\ )\   ((_))((_)\ _ )(_)|_))\ /((_)
 * ((_|(_)  _| (_))((_) ((_)__)((_|_))
 * / _/ _ \/ _` / -_|_-< / _` \ V // -_)
 * \__\___/\__,_\___/__/_\__,_|\_/ \___|
 *
 * 东隅已逝，桑榆非晚。(The time has passed,it is not too late.)
 * 虽不能至，心向往之。(Although I can't, my heart is longing for it.)
 *
 */

package org.mac.explorations.corejava.concurrent.forkjoin;

import java.util.Random;

/**
 * @auther mac
 * @date 2019-12-04
 */
public final class Utils {
    private Utils(){throw new UnsupportedOperationException("Can't instantiate");}

    /**
     * 初始化一个数组
     *
     * @param length
     * @return
     */
    public static int[] buildRandomIntArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = new Random().nextInt(100);
        }
        return array;
    }

    public static long rangSum(int[] array,int lo,int hi) {
        long sum = 0L;
        for (int i = lo; i <= hi; i++) {
            sum += array[i];
        }
        return sum;
    }

}
