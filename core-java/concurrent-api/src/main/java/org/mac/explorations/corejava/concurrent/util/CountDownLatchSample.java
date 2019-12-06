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

package org.mac.explorations.corejava.concurrent.util;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther mac
 * @date 2019-12-02
 */
public class CountDownLatchSample {
    public static void main(String[] args) {
        final int [] data = new int[] {1,2,3,4,5,6,7,8,9,10};
        CountDownLatch latch = new CountDownLatch(data.length);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < data.length; i++) {
            final int index = i;
            executor.execute(()->{
                data[index] = (data[index]%2 == 0)?data[index]*2:data[index]*30;
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(data));
        executor.shutdown();

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0 ; i < 10; i++) {
            new Thread(()->{
                try {
                    System.out.println(Thread.currentThread().getName()+" await");
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            }).start();
            countDownLatch.countDown();
        }
    }
}
