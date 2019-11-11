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

package org.mac.explorations.algs.binarysearch;

import java.util.Objects;

/**
 * 二分搜索
 *
 * @auther mac
 * @date 2019-11-11
 */
public abstract class UtilityBinarySearch {
    /**
     * 有序的数组中用二分搜索搜索给定值的索引
     * 没有返回-1;
     *
     * @param data
     * @param key
     * @param <E>
     * @return
     */
    public static<E extends Comparable<E>> int getIndex(E[] data,E key){
        Objects.requireNonNull(data);
        Objects.requireNonNull(key);

        int left = 0,right = data.length - 1;
        while (left <= right) {
            int mid = left + (( right - left) >> 1);
            if (data[mid].compareTo(key) > 0){
               right = mid - 1;
            }
            else if (data[mid].compareTo(key) < 0) {
               left = mid + 1;
            }
            else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 有序的数组中用二分搜索搜索给定值的索引
     * 没有返回-1  - 使用递归
     *
     * @param data
     * @param key
     * @param <E>
     * @return
     */
    private static<E extends Comparable<E>> int getIndexUsingRecursion(E[] data,E key){
        Objects.requireNonNull(data);
        Objects.requireNonNull(key);

        return  binarySearch(data,key,0,data.length -1);
    }

    private static <E extends Comparable<E>> int binarySearch(E[] data, E key, int left, int right) {
        if (left > right){
            return -1;
        }

        int mid = left + (( right - left) >> 1);
        if (data[mid].compareTo(key) > 0){
            return binarySearch(data,key,left,mid - 1);
        }
        else if (data[mid].compareTo(key) < 0) {
            return binarySearch(data,key,mid + 1,right);
        }
        else {
            return mid;
        }
    }

    public static void main(String[] args) {
        Integer[] data = new Integer[]{140,270,339,371,427,731,777,800,908,915};
        System.out.println(UtilityBinarySearch.getIndex(data,777));
        System.out.println(UtilityBinarySearch.getIndexUsingRecursion(data,777));
    }
}
