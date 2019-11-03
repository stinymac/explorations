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
        int n = 1000000;
        MaximumHeap<Integer> heap = new MaximumHeap<>(n);
        //int[] a = {52,64,41,30,28,16,22,13,19,17,15};
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            heap.add(random.nextInt(Integer.MAX_VALUE));//
        }

        System.out.println("size:"+heap.size());

        List<Integer> sortList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            sortList.add(heap.take());
        }


        for (int i = 1; i < n; i++) {
            if (sortList.get(i-1) - sortList.get(i) < 0) {
                throw new RuntimeException("Error");
            }
        }
        System.out.println("success");
    }
}
