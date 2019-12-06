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

package org.mac.explorations.corejava.concurrent.basics.jmm;

import java.util.concurrent.CountDownLatch;

/**
 * 重排序
 *
 * 程序在不考虑重排序的情况下有以下三种结果
 *
 * [a = 1;x = b; b = 1;y = a;] ---> (x = 0, y = 1)
 * [b = 1;y = a; a = 1;x = b;] ---> (x = 1, y = 0)
 * [a = 1;b = 1; x = b;y = a;] ---> (x = 1, y = 1)
 *
 * 一种重排序结果
 * [y = a;a = 1;x = b;b = 1;] ---> (x = 0, y = 0)
 *
 * @auther mac
 * @date 2019-11-25
 */
public class OutOfOrderExecution {

    private static int x = 0,y = 0;
    private static int a = 0,b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (;;) {
            CountDownLatch latch = new CountDownLatch(1);

            Thread t1 = new Thread(()->{
                latchWait(latch);
                a = 1;
                x = b;
            });

            Thread t2 = new Thread(()->{
                latchWait(latch);
                b = 1;
                y = a;
            });
            t1.start();
            t2.start();

            i++;
            reset();
            latch.countDown();
            t1.join();
            t2.join();
            System.out.println("第 " + i + " 次执行结果:( x = " + x + ", y = "+ y +")");
            if (x == 0 && y == 0) {
                break;
            }
        }
    }

    private static void latchWait(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void reset() {
        x = 0;
        y = 0;
        a = 0;
        b = 0;
    }
}
