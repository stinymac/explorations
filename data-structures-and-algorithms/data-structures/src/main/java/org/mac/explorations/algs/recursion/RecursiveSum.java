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

package org.mac.explorations.algs.recursion;

import java.util.Objects;

/**
 * 递归求和
 *
 * @auther mac
 * @date 2019-10-25
 */
public class RecursiveSum {

    public static long sum (int[] array) {

        Objects.requireNonNull(array);

        return sum(array,0);
    }

    /**
     * 使用递归的方式求和
     *
     * 递归分解:
     *
     * Sum(array[0,n-1]) = array[0] + array[1] ......array[n-1];
     *
     *
     * Sum(array[x,n-1]) = array[x] + array[x+1] ......array[n-1] //0 < x < n-1
     *
     * Sum(array[0,n-1]) = array[0] + Sum(array[1,n-1])
     *
     * Sum(array[1,n-1]) = array[1] + Sum(array[2,n-1])
     *
     * ....
     *
     * Sum(array[n-1,n-1]) = array[n-1] + 0
     *
     * @param array
     * @param startIndex
     * @return
     */
    private static long sum(int[] array, int startIndex) {

        if (startIndex == array.length) {
            return 0;
        }
        return array[startIndex] + sum(array,startIndex + 1);
    }

    public static void main(String[] args) {
        System.out.println(RecursiveSum.sum(new int[]{1,2,3,4,5,6,7,8,9,10}));
    }
}
