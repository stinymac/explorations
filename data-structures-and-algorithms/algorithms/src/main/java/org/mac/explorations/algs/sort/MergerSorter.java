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
 * 归并排序
 *
 * @auther mac
 * @date 2019-11-10
 */
public class MergerSorter<T extends Comparable<T>> implements Sorter<T>  {
    @Override
    public void sort(Comparable[] array) {
        Objects.requireNonNull(array);
        //sortBottomUp(array);
        sort(array,0,array.length-1);
    }

    private static final int INSERTION_SORT_THRESHOLD = 47;
    /**
     * 自顶向下的归并排序
     *
     * @param array
     * @param left
     * @param right
     */
    private void sort(Comparable[] array, int left, int right) {

        /*if (left >= right){
            return;
        }*/
        //优化-当元素数量小于一定规模时使用插入排序
        if (right - left <= INSERTION_SORT_THRESHOLD) {
           for (int m = left + 1; m <= right; m++){
               Comparable t = array[m];
               int n = m;
               for (; n > left && t.compareTo(array[n-1]) < 0;n--) {
                  array[n] = array[n-1];
               }
               array[n] = t;
           }
           return;
        }
        // 防止(left+right)溢出
        int mid = left + ( (right - left) >> 1);
        sort(array,left,mid);
        sort(array,mid + 1,right);
        //归并
        if (array[mid] .compareTo( array[mid+1]) > 0) { // 归并的左右两边是有序的
            merge(array, left, mid, right);
        }
    }

    /**
     * 归并排序结果
     *
     * @param array
     * @param left
     * @param mid
     * @param right
     */
    private void merge(Comparable[] array, int left, int mid, int right) {

        // 结果归并临时空间
        Comparable[] aux =  new Comparable[right - left + 1];

        for(int k = 0,i = left, j = mid + 1; k < aux.length;k++){
            if (i > mid) {
                aux[k] = array[j];
                j++;
            }
            else if (j > right){
                aux[k] = array[i];
                i++;
            }
            else if (array[i].compareTo(array[j]) < 0) {
                aux[k] = array[i];
                i++;
            } else {
                aux[k] = array[j];
                j++;
            }
        }

        for (int i = 0; i < aux.length;i++,left++) {
            array[left] = aux[i];
        }
    }

    /**
     * 自底向上的归并排序
     *
     * @param array
     */
    private void sortBottomUp(Comparable[] array) {
        Objects.requireNonNull(array);
        for (int sz = 1 ; sz < array.length; sz += sz) {
            for (int i = 0; i+sz < array.length; i+=sz+sz){
                merge(array,i,i+sz-1,Math.min(i+sz+sz-1,array.length-1));
            }
        }
    }
}
