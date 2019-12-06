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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther mac
 * @date 2019-12-01
 */
public class ReentrantLockSample {

    public static void main(String[] args) throws InterruptedException {
        final Lock lock = new ReentrantLock();

        new Thread(() -> {
            try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + ": I got lock....");

                Thread.sleep(80000);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + ": I release lock....");
        },"T-1").start();

        Thread.sleep(1000);

        new Thread(() -> {
            boolean heldLock = false;
            try {
                if(heldLock = lock.tryLock()) {
                    System.out.println(Thread.currentThread().getName() + ": I got lock....");
                }
            } catch (Exception e) {

            } finally {
                if (heldLock) {
                    lock.unlock();
                }
            }
            System.out.println(Thread.currentThread().getName() + ": I release lock....");
        },"T-2").start();
    }
}
