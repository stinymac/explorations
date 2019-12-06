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

import java.util.concurrent.TimeUnit;

/**
 * volatile 关键字可见性
 *
 * @auther mac
 * @date 2019-11-25
 */
public class VisibilityExample {

    //private static int initValue = 0;
    /**
     * volatile: 保证了共享变量在不同线程之间的可见性
     *           禁止了对其的重排序(不会将其前面的指令放到其后，也不会将其后面的指令放到其前面)保证了有序性
     */
    private static volatile int initValue = 0;
    private static final int LIMIT_VALUE = 5;

    public static void main(String[] args) {
        /**
         * 这个线程里没有对initValue的写操作
         * JVM会对程序做优化，仅一次将将initValue读入本地内存后
         * 不再从共享主内存中取值 在不加volatile修饰initValue 这个线程
         * 感知不到initValue的更新
         */
        new Thread(() -> {
            int localValue = initValue;
            while (localValue < LIMIT_VALUE) {
                if (localValue != initValue) {
                    System.out.println("The value updated to:"+initValue);
                    localValue = initValue;
                }
            }
        },"READER").start();

        new Thread(() -> {
            int localValue = initValue;
            while (initValue < LIMIT_VALUE) {
                System.out.println("Updated value to:"+(++localValue));
                initValue = localValue;
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"UPDATER").start();
    }
}
