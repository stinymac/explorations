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

import java.util.concurrent.TimeUnit;

/**
 * 哲学家就餐死锁问题
 *
 * @auther mac
 * @date 2019-11-26
 */
public class DiningForPhilosophers {

    static class Philosopher extends Thread {

        private Object leftChopstick;
        private Object rightChopstick;

        public Philosopher(String name,Object leftChopstick, Object rightChopstick) {
            super(name);
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        @Override
        public void run() {
            while (true) {
                action("thinking...");
                synchronized (leftChopstick) {
                    System.out.println(this.getName() + " got the left chopstick");
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (rightChopstick) {
                        System.out.println(this.getName() + " got the right chopstick");
                        action(this.getName() + " eating...");
                    }
                }
                System.out.println(this.getName()+" finished eating");
            }
        }

        public void action(String action) {
            System.out.println(this.getName() + " do "+ action);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int philosopherNum = 5;

        Philosopher[] philosophers = new Philosopher[philosopherNum];
        Object[] chopsticks = new Object[philosophers.length];
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object leftChopstick = chopsticks[i];
            Object rightChopstick = chopsticks[(i+1)%philosophers.length];
            /**
             * 让一个哲学家与其他哲学家取筷子的顺序相反而避免死锁
             */
            if (i == 0) {
                philosophers[i] = new Philosopher("P" + (i + 1), rightChopstick, leftChopstick);
            }
            else {
                philosophers[i] = new Philosopher("P" + (i + 1), leftChopstick, rightChopstick);
            }
            philosophers[i].start();
        }
    }
}
