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

package org.mac.explorations.algs.ds.queue;

import java.util.Random;

/**
 * @auther mac
 * @date 2019-10-23
 */
public class Main {

    public static void main(String[] args) {

        /*Queue<Integer> arrayQueue = new ArrayQueue<>();

        for (int i = 0 ; i < 10; i++) {
            if (i % 3 == 2) {
                arrayQueue.dequeue();
            }
            arrayQueue.enqueue(i);

            System.out.println(arrayQueue);
        }*/
        /*
        Queue<Integer> circularQueue = new CircularQueue<>();

        for (int i = 0 ; i < 50; i++) {
            if (i % 3 == 2) {
                circularQueue.dequeue();
            }
            circularQueue.enqueue(i);

            System.out.println(circularQueue);
        }

        System.out.println("======================================================================");

        for (int i = 0; i < 30; i ++) {
            circularQueue.dequeue();
            System.out.println(circularQueue);
        }*/

        /*
        Queue<Integer> linkedListQueue = new LinkedListQueue<>();

        for (int i = 0 ; i < 50; i++) {
            if (i % 3 == 2) {
                linkedListQueue.dequeue();
            }
            linkedListQueue.enqueue(i);

            System.out.println(linkedListQueue);
        }*/


        Queue<Integer> arrayQueue = new ArrayQueue<>();
        Queue<Integer> circularQueue = new CircularQueue<>();
        Queue<Integer> linkedListQueue = new LinkedListQueue<>();

        int numberOfOperations = 1000000;

        /**
         * ArrayQueue cost:128.61859053 s
         * CircularQueue cost:0.116065703 s  -- 数组扩容
         * LinkedListQueue cost:0.120641183 s -- 创建新节点(new)
         */
        System.out.println("ArrayQueue cost:" + simpleQueuePerformance(numberOfOperations,arrayQueue)+" s");
        System.out.println("CircularQueue cost:" + simpleQueuePerformance(numberOfOperations,circularQueue)+" s");
        System.out.println("LinkedListQueue cost:" + simpleQueuePerformance(numberOfOperations,linkedListQueue)+" s");
    }

    private static double simpleQueuePerformance(int numberOfOperations,Queue<Integer> queue) {
        Random random = new Random();
        long startTime = System.nanoTime();
        for (int i = 0 ; i < numberOfOperations; i++ ) {
            queue.enqueue(random.nextInt(Integer.MAX_VALUE));
        }
        for (int i = 0 ; i < numberOfOperations; i++ ) {
            queue.dequeue();
        }
        long endTime = System.nanoTime();

        return ( endTime - startTime ) / 1000000000.0;
    }
}
