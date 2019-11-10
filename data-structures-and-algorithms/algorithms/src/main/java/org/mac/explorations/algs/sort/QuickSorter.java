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

package org.mac.explorations.algs.sort;

import java.util.Objects;
import java.util.Random;

/**
 * 快速排序
 *
 * @auther mac
 * @date 2019-11-10
 */
public class QuickSorter<T extends Comparable<T>> implements Sorter<T>{

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    @Override
    public void sort(T[] array) {
        Objects.requireNonNull(array);
        sort(array,0,array.length-1);
    }

    /**
     * 快速排序
     *
     * @param array
     * @param left
     * @param right
     */
    private void sort(T[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        //int p = twoWayPartition(array,left,right); //partition(array,left,right);
        int[] ps = threeWayPartition(array,left,right);
        sort(array,left,ps[0]-1);
        sort(array,ps[1],right);
    }

    /**
     * 将数组按小于选定元素和大于选定元素
     * 划分为2部分，并返回选定元素的位置。
     *
     * array[left].....array[p-1] < array[p] < array[p+1].....array[right]
     *
     * @param array
     * @param left
     * @param right
     * @return
     */
    private int partition(T[] array, int left, int right) {
        // 直接选择第一个元素 在数组有序的情况下 时间复杂度退化O(n^2)
        T v = selectTargetElementRandomly(array,left,right);
        int j = left;
        for (int i = left+1;i <= right;i++) {
            // 当数组中有极大量的重复数据时 时间复杂度退化O(n^2)
            if (array[i] .compareTo(v) < 0){
                T t = array[i];
                array[i] = array[++j];
                array[j] = t;
            }
        }
        T t = array[j];
        array[j] = array[left];
        array[left] = t;

        return j;
    }

    /**
     * 双路划分
     * 防止数组中有极大量的重复数据时的不平衡划分
     *
     * @param array
     * @param left
     * @param right
     * @return
     */
    private int twoWayPartition(T[] array, int left, int right) {
        T v = selectTargetElementRandomly(array,left,right);
        int  j = left+1,i = right;
        while (true) {
            while (j <= right && array[j].compareTo(v) <= 0) j++;
            while (i >= left + 1 && array[i].compareTo(v) >= 0) i--;
            if (j > i) break;
            T t = array[j];
            array[j] = array[i];
            array[i] = t;
        }
        T t = array[i];
        array[i] = array[left];
        array[left] = t;
        return i;
    }

    /**
     * 双路划分
     * 防止数组中有极大量的重复数据时的不平衡划分
     *
     * @param array
     * @param left
     * @param right
     * @return
     */
    private int[] threeWayPartition(T[] array, int left, int right) {
        T v = selectTargetElementRandomly(array,left,right);
        int lt = left,i = left + 1,gt = right+1;
        while (i < gt) {
            if (array[i].compareTo(v) < 0) {
                T t = array[lt+1];
                array[lt+1] = array[i];
                array[i] = t;
                lt++;
                i++;
            }
            else if(array[i].compareTo(v) > 0) {
                T t = array[gt-1];
                array[gt-1] = array[i];
                array[i] = t;
                gt--;
            }
            else {
                i++;
            }
        }
        T t = array[lt];
        array[lt] = array[left];
        array[left] = t;

        return new int[]{lt,gt};
    }

    /**
     * 随机的选择一个比较标的元素
     *
     * @param left
     * @param right
     * @return
     */
    public T selectTargetElementRandomly(T[] array,int left,int right) {
        int index = RANDOM.nextInt(right) %(right-left) + left;
        T t = array[index];
        array[index] =array[left];
        array[left] = t;
        return t;
    }
}
