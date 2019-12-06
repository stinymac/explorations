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

package org.mac.explorations.corejava.concurrent.basics.thread.methods;

/**
 * join 方法原理
 *
 * @auther mac
 * @date 2019-11-24
 */
public class JoinPrinciple {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() ->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" 线程结束");
        });
        t.start();
        System.out.println(Thread.currentThread().getName()+" 线程等待");
        //t.join();
        /**
         * 以t为锁 t.wait() 使调用者线程(这里是main线程)进入等待状态
         * 而JVM会在线程的run方法执行结束后调用notifyAll,即t线程结束后
         * 会唤醒等待的main线程
         */
        synchronized (t) {
            t.wait();
        }
        System.out.println(Thread.currentThread().getName()+" 等待结束");
    }
}
