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

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @auther mac
 * @date 2019-11-09
 */
public class Tester {


    public static void main(String[] args) {

        int size = 10000,min = 0,max = size;

        Integer[] array = generateRandomIntegerArray(size,min,max);
        Integer[] insertion = Arrays.copyOf(array,array.length);
        Integer[] bubble = Arrays.copyOf(array,array.length);
        Integer[] shell = Arrays.copyOf(array,array.length);
        Integer[] merger = Arrays.copyOf(array,array.length);
        Integer[] quick = Arrays.copyOf(array,array.length);
        Integer[] heap = Arrays.copyOf(array,array.length);
        testSorter(new SelectionSorter<>(),array);
        testSorter(new InsertionSorter<>(),insertion);
        testSorter(new BubbleSorter<>(),bubble);
        ShellSorter shellSorter = new ShellSorter();
        testSorter(shellSorter,shell);
        testSorter(new MergerSorter<>(),merger);
        testSorter(new QuickSorter<>(),quick);
        testSorter(new HeapSorter<>(),heap);

        //shellSorter.sort2(array);
        //System.out.println(isSorted(array));

    }

    private static <T extends Comparable<T>> void testSorter(Sorter<T> sorter,T[] array) {
        String sorterAlgorithmsName = sorter.getClass().getSimpleName();
        long startTime = System.nanoTime();
        sorter.sort(array);
        double cost = (System.nanoTime() - startTime)/1000000000.0;
        System.out.println(String.format("%15s" + " cost:"+"%7s s, and is sorted?:"+ isSorted(array) ,sorterAlgorithmsName,String.valueOf(cost).substring(0,7)));
    }

    private static <T extends Comparable<T>> boolean isSorted(T[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i-1].compareTo(array[i]) > 0){
                return false;
            }
        }
        return true;
    }

    private static Integer[] generateRandomIntegerArray(int size,int rangMin,int rangMax) {
        assert size > 0;
        assert rangMin <= rangMax;
        Integer[] array = new Integer[size];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(rangMax) % (rangMax - rangMin + 1) + rangMin;
        }
        return array;
    }
}
