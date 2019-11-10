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

/**
 * 希尔排序
 * int n = arr.length;

 // 计算 increment sequence: 1, 4, 13, 40, 121, 364, 1093...
 int h = 1;
 while (h < n/3) {
 h = 3*h + 1;
 }

 while (h >= 1) {

 // h-sort the array
 for (int i = h; i < n; i++) {

 // 对 arr[i], arr[i-h], arr[i-2*h], arr[i-3*h]... 使用插入排序
 Comparable e = arr[i];
 int j = i;
 for ( ; j >= h && e.compareTo(arr[j-h]) < 0 ; j -= h)
 arr[j] = arr[j-h];
 arr[j] = e;
 }

 h /= 3;
 }
 * @auther mac
 * @date 2019-11-09
 */
public class ShellSorter<T extends Comparable<T>> implements Sorter<T>  {
    /**
     ==================================version-1===================================
    @Override
    public void sort(T[] array) {

        Objects.requireNonNull(array);
        int h = array.length >> 1;
        while (h != 1) {
           int partSize =  array.length / h;
           for (int i = 0; i < h; i++) {
               sort(array,partSize,i,h);
           }
           h = h >> 1;
        }
        sort(array,array.length,0,1);
    }

    private void sort(T[] array, int partSize, int startIndex, int h) {
        for (int i = startIndex + h; i < partSize * h; i = i + h) {
            T temp = array[i];
            for (int j = i; j > startIndex; j = j - h) {
                if (temp.compareTo(array[j - h]) < 0) {
                    array[j] = array[j - h];
                }
                else {
                    array[j] = temp;
                    break;
                }
            }
        }
    }*/

    /**
     * ================================version-2===================================
     */
    @Override
    public void sort(T[] array) {
        Objects.requireNonNull(array);
        // increment sequence: 1, 4, 13, 40, 121, 364, 1093...
        int h = 1;
        while (h < array.length/3) {
            h = 3*h + 1;
        }
        while (h >= 1) {

            // h-sort the array
            for (int i = h; i < array.length; i++) {

                // 对 array[i], array[i-h], array[i-2*h], array[i-3*h]... 使用插入排序
                T e = array[i];
                int j = i;
                for ( ; j >= h && e.compareTo(array[j-h]) < 0 ; j -= h)
                    array[j] = array[j-h];
                array[j] = e;
            }
            h /= 3;
        }
    }

    public void sort2(T[] array) {
        Objects.requireNonNull(array);
        int h = 1;
        while (h < array.length/3) { h = 3*h + 1; }
        while (h >= 1) {
            // h-sort the array
            for (int i = h; i < array.length; i++) {

                // 对 array[i], array[i-h], array[i-2*h], array[i-3*h]... 使用插入排序
                T e = array[i];

                for ( int j = i; j >= h ; j -= h){
                    if (e.compareTo(array[j-h]) < 0 ) {
                        array[j] = array[j-h];
                    }
                    else {
                        array[j] = e;
                        break;
                    }
                }
            }
            h /= 3;
        }
    }
}
