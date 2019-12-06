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

import java.util.concurrent.Semaphore;

/**
 * 使用Semaphore简单模拟限流
 *
 * @auther mac
 * @date 2019-12-02
 */
public class SimpleCurrentLimitingSimulationWithSemaphore {

    private static final int MAX_REQUEST_ALLOWED = 2;
    private static final int NUM_OF_REQUEST = 10;

    private static final Semaphore LIMITING = new Semaphore(MAX_REQUEST_ALLOWED);

    public static void main(String[] args) {
        for (int i = 0; i < NUM_OF_REQUEST; i++) {
            new Thread(() -> {
                try {
                    LIMITING.acquire();
                    System.out.println(Thread.currentThread().getName()+":got permit and running...");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LIMITING.release();
                }
            },"T-"+(i+1)).start();
        }
    }
}
