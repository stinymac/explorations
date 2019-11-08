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

package org.mac.explorations.algs.ds.tree.rbt;

import java.util.Random;

/**
 * @auther mac
 * @date 2019-11-07
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("RBT:"+ testRBTree(10000));
    }

    private static double testRBTree(int nums) {
        //Random random = new Random();
        long startTime = System.nanoTime();
        RBTree<Integer,Integer> avl = new RBTree<>();
        for (int i = 0; i < nums; i++){
            avl.put(i,i);
        }
        return (System.nanoTime() - startTime ) / 1000000000.0;
    }
}
