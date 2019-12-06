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

package org.mac.explorations.corejava.concurrent.basics.thread.lifecycle;

/**
 * @auther mac
 * @date 2019-11-23
 */
public class ThreadLifecycleStates {

    private static Long i = 0L;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            System.out.println("...");
        });
        System.out.println(t.getState());
        t.start();
        System.out.println(t.getState());
        t.join();
        System.out.println(t.getState());


        Runnable runnable = () -> {
            synchronized (ThreadLifecycleStates.class) {
                while (true) {
                    System.out.println("currentThread:" + Thread.currentThread().getName());
                    i++;
                    System.out.println("currentThread:" + Thread.currentThread().getName() + " and current value:"+i);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("-->currentThread:" + Thread.currentThread().getName()+" wake up");
                }
            }
        };

        Thread t1 = new Thread(runnable,"t1");
        Thread t2 = new Thread(runnable,"t2");
        Thread t3 = new Thread(runnable,"t3");

        t1.start();
        Thread.sleep(1000);
        t2.start();
        System.out.println("t1.getState():" + t1.getState());
        System.out.println("t2.getState():" + t2.getState());
        Thread.sleep(1000);
        System.out.println("-------------------------------");
        t3.start();
        while (true) {
            System.out.println("t1.getState():" + t1.getState());
            System.out.println("t2.getState():" + t2.getState());
            System.out.println("t3.getState():" + t3.getState());
            Thread.sleep(1000);
        }
    }
}
