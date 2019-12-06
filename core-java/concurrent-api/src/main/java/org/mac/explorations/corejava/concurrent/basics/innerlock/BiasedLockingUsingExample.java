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

package org.mac.explorations.corejava.concurrent.basics.innerlock;

import java.util.Vector;

/**
 * @auther mac
 * @date 2019-11-29
 */
public class BiasedLockingUsingExample {

    public static Vector<Integer> vector = new Vector<Integer>();

    /**
     * 开启偏向锁(默认开启)：-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
     * 关闭偏向锁：-XX:-UseBiasedLocking
     *
     * @param args
     */
    public static void main(String[] args){
        long begin = System.currentTimeMillis();
        int count = 0;
        int num = 0;
        while(count < 10000000){
            vector.add(num);
            num = num + 5;
            count++;
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}
