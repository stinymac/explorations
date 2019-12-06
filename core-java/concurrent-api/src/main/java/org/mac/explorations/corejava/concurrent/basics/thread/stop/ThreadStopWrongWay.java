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

package org.mac.explorations.corejava.concurrent.basics.thread.stop;

/**
 *
 * @auther mac
 * @date 2019-11-23
 */
public class ThreadStopWrongWay extends Thread {
    private volatile boolean canceled = false;

    @Override
    public void run() {
        while (!canceled) {
            System.out.println("running...");
            synchronized (this) {
                try {
                    Thread.currentThread().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("over...");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadStopWrongWay t = new ThreadStopWrongWay();
        t.start();
        Thread.sleep(500);
        t.canceled = true;

        System.out.println("canceled:"+t.canceled);
    }
}
