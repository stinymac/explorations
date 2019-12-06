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

package org.mac.explorations.corejava.concurrent.basics.thread.security;

/**
 * 多线程死锁
 *
 * @auther mac
 * @date 2019-11-25
 */
public class MultithreadingDeadlock {

    private static class Task implements Runnable {

        private final  Object lockA;
        private final  Object lockB;

        public Task(Object lockA, Object lockB) {
            this.lockA = lockA;
            this.lockB = lockB;
        }

        @Override
        public void run() {
            doTask();
        }
        private void doTask() {
            synchronized (lockA) {
                System.out.println("got innerlock A (hash code:"+lockA.hashCode()+") and running...");
                synchronized (lockB) {
                    System.out.println("got innerlock B(hash code:"+lockB.hashCode()+") and running...");
                }
            }
        }
    }

    public static void main(String[] args) {

        final Object lockA = new Object();
        final Object lockB = new Object();

        new Thread(new Task(lockA,lockB)).start();
        new Thread(new Task(lockB,lockA)).start();
    }
}
