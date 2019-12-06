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

package org.mac.explorations.corejava.concurrent.forkjoin.real;

import org.mac.explorations.corejava.concurrent.forkjoin.Utils;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Fork/Join 归并排序
 *
 * ForkJoin框架处理的任务基本都能使用递归处理，
 * 比如求斐波那契数列等，但递归算法的缺陷是：
 *
 * 一是会只用单线程处理，
 * 二是递归次数过多时会导致堆栈溢出；
 *
 * ForkJoin解决了这两个问题，使用多线程并发处理，充分利用计算资源来提高效率，
 * 同时避免堆栈溢出发生。当然像求斐波那契数列这种小问题直接使用线性算法可能更简单，
 * 实际应用中完全没必要使用ForkJoin框架，ForkJoin是用来处理超大型问题的，比如超大数组排序。
 * 其最佳应用场景：多核、多内存、可以分割计算再合并的计算密集型任务
 *
 * @auther mac
 * @date 2019-12-05
 */
public class MergeSortWithForkJoin {
    /**
     * 归并排序
     *
     * @param array
     */
    public static void mergeSort(int[] array,int left,int right) {
        if(right <= left) {
            return;
        }
        int mid = left + ((right - left) >> 1);
        mergeSort(array,left,mid);
        mergeSort(array,mid+1,right);

        merge(array,left,mid,right);
    }
    /**
     * 对左右两边分别有序的排序结果做归并
     *
     * @param array
     * @param left
     * @param mid
     * @param right
     * @return
     */
    public static void merge(int[] array,int left,int mid,int right) {
        if (array == null || !(left >= 0 && left <= mid && mid <= right && right < array.length))
            throw new IllegalArgumentException("error arg");

        int[] aux = new int[right - left + 1];

        for (int k = 0,i = left,j = mid + 1; k < aux.length; k++) {
            if (i > mid) {
                aux[k] = array[j++];
            } else if (j > right) {
                aux[k] = array[i++];
            }else if (array[i] <= array[j]) {
                aux[k] = array[i++];
            }
            else {
                aux[k] = array[j++];
            }
        }
        for (int k = 0; k < aux.length; k++) {
            array[left++] =aux[k];
        }
    }

    static class MergeSortRecursiveAction extends RecursiveAction {

        private final int[] array;
        private final int left;
        private final int right;

        MergeSortRecursiveAction(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right <= left) {
                return;
            }

            int mid = this.left + ((this.right - this.left) >> 1);
            MergeSortRecursiveAction leftAction = new MergeSortRecursiveAction(array,left,mid);
            MergeSortRecursiveAction rightAction = new MergeSortRecursiveAction(array,mid+1,right);

            leftAction.fork();
            rightAction.fork();

            leftAction.join();
            rightAction.join();

            merge(this.array,this.left, mid,this.right);
        }
    }

    public static boolean isSorted(int[] array){
        for (int i = 1; i < array.length;i++) {
            if (array[i-1] > array[i]){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] array = Utils.buildRandomIntArray(40000000);
        int[] copyArray = Arrays.copyOf(array,array.length);

        long startTimestamp = System.nanoTime();
        mergeSort(array,0,array.length-1);
        long endTimestamp = System.nanoTime();
        if (!isSorted(array)) {
            System.err.println("single thread merge sort error!");
        }
        else {
            System.out.println("single thread merge sort cost:"+(endTimestamp - startTimestamp)/1000000000D + " s");
        }

        MergeSortRecursiveAction recursiveAction = new MergeSortRecursiveAction(copyArray,0,copyArray.length-1);
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() + 1);
        startTimestamp = System.nanoTime();
        forkJoinPool.invoke(recursiveAction);
        endTimestamp = System.nanoTime();
        if (!isSorted(copyArray)) {
            System.err.println("fork/join merge sort error!");
        }
        else {
            System.out.println("fork/join merge sort cost:"+(endTimestamp - startTimestamp)/1000000000D + " s");
        }
    }
}
