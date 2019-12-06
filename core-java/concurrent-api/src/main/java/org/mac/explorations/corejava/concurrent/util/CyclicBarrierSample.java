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

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @auther mac
 * @date 2019-12-02
 */
public class CyclicBarrierSample {

    public static void main(String[] args){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10,() -> {
            System.out.println(Thread.currentThread().getName()+ " all tread ready...");
        });
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName()+" ready");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            },"T-"+(i+1)).start();
        }
    }
}
