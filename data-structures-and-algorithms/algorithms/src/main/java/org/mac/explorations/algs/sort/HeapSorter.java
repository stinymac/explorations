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

import org.mac.explorations.algs.ds.heap.MaximumHeap;

import java.util.Objects;

/**
 * 堆排序
 *
 * @auther mac
 * @date 2019-11-10
 */
public class HeapSorter<T extends Comparable<T>> implements Sorter<T>  {
    @Override
    public void sort(T[] array) {
        Objects.requireNonNull(array);
        /*MaximumHeap<T> heap = new MaximumHeap(array);
        for (int i = array.length - 1; i >= 0; i--){
            array[i] = heap.take();
        }*/
        /**
         * 原地堆排序-堆构建
         */
        for (int i = (array.length - 1 - 1 ) >> 1; i >= 0; i--) {
            shiftDown(array,array.length,i);
        }
        /**
         * 原地堆排序-值交换
         */
        for (int k = array.length - 1; k > 0; k--) {
            T t = array[k];
            array[k] = array[0];
            array[0] = t;
            shiftDown(array,k,0);
        }
    }

    private void shiftDown(T[] array,int length, int index) {

        while ((index << 1) + 1 < length) {

            int leftChildIndex  = (index << 1) + 1;
            int rightChildIndex = (index << 1) + 2;
            //左右子元素中的较大元素索引
            int largerChildIndex = (rightChildIndex < length
                    && array[leftChildIndex].compareTo(array[rightChildIndex]) < 0)
                    ? rightChildIndex:leftChildIndex;

            if (array[index].compareTo(array[largerChildIndex]) < 0) {
                T t = array[index];
                array[index] = array[largerChildIndex];
                array[largerChildIndex] = t;
                index = largerChildIndex;
            }
            else {
                break;
            }
        }
    }
}
