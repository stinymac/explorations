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

package org.mac.explorations.algs.ds.heap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @auther mac
 * @date 2019-11-02
 */
public class Main {

    public static void main(String[] args) {
        int n = 10;
        //MaximumHeap<Integer> heap = new MaximumHeap<>(n);
        IndexMaximumHeap<Integer> heap = new IndexMaximumHeap<>(n);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            heap.add(random.nextInt(1<<n));//
        }
        heap = new IndexMaximumHeap<>(new Integer[]{777,915,339,270,731,140,371,800,427,908});
        System.out.println(heap);
        System.out.println(heap.set(5,1000));
        System.out.println(heap);
        heap.take();
        heap.take();
        System.out.println(heap);
        heap.add(781);
        System.out.println(heap);
        /*List<Integer> sortList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            sortList.add(heap.take());
        }

        for (int i = 1; i < n; i++) {
            if (sortList.get(i-1) - sortList.get(i) < 0) {
                throw new RuntimeException("Error");
            }
        }
        System.out.println("success");
        System.out.println(heap);*/
        //System.out.println(heap.set(5,100));
    }
}
