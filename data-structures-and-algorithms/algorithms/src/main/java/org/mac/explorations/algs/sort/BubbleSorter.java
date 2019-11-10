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
 * 冒泡排序
 *
 * @auther mac
 * @date 2019-11-09
 */
public class BubbleSorter<T extends Comparable<T>> implements Sorter<T> {
    @Override
    public void sort(T[] array) {
        Objects.requireNonNull(array);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j <  array.length - i - 1; j++){
                if (array[j].compareTo(array[j+1]) > 0) {
                    T t = array[j];
                    array[j] = array[j+1];
                    array[j+1] = t;
                }
            }
        }
    }
}
