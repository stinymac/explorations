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
 * 选择排序
 *
 * @auther mac
 * @date 2019-11-09
 */
public class SelectionSorter<T extends Comparable<T>> implements Sorter<T> {
    /**
     * 使用选择排序对数组排序
     *
     * @param array
     */
    @Override
    public void sort(T [] array) {

        Objects.requireNonNull(array);

        for (int i = 0 ; i < array.length; i++) {

            int minElementIndex = i;

            for (int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[i]) < 0) {
                    minElementIndex = j;
                    T t = array[i];
                    array[i] = array[minElementIndex];
                    array[minElementIndex] = t;
                }
            }
        }
    }
}
