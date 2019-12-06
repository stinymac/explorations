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

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 多线程活锁
 *
 * @auther mac
 * @date 2019-11-26
 */
public class MultithreadingLiveLock {

    static class Spoon {

        private Diner owner;

        public Spoon(Diner owner) {
            this.owner = owner;
        }

        public void setOwner(Diner owner) {
            this.owner = owner;
        }

        public void use () {
            System.out.println(owner.name+" use spoon");
        }
    }

    static class Diner {

        private static final Random rand = new Random(System.currentTimeMillis());

        private String name;
        private volatile boolean isHungry = true;

        public Diner(String name) {
            this.name = name;
        }

        public void eatWith(Spoon spoon, Diner companion) {

            while (isHungry) {
                synchronized (spoon) {
                    if (spoon.owner != this) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    /**
                     * (rand.nextInt(Integer.MAX_VALUE) & 1) == 0 随机值 解决循环让出spoon
                     */
                    if (companion.isHungry && (rand.nextInt(Integer.MAX_VALUE) & 1) == 0) {
                       spoon.setOwner(companion);
                       System.out.println(name+": you first. dear "+companion.name);
                       continue;
                    }

                    spoon.use();
                    spoon.setOwner(companion);
                    isHungry = false;
                    System.out.println(name +": I finished eating");
                }
            }
        }
    }

    public static void main(String[] args) {

        Diner master = new Diner("master");
        Diner guest  = new Diner("guest");

        Spoon spoon = new Spoon(guest);

        new Thread(() -> {
            guest.eatWith(spoon,master);
        }).start();

        new Thread(() -> {
            master.eatWith(spoon,guest);
        }).start();
    }
}
