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

package org.mac.explorations.algs.ds.tree.avl;

import org.mac.explorations.algs.ds.map.BSTMap;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @auther mac
 * @date 2019-11-06
 */
public class Main {
    public static void main(String[] args) {
        // 在本机可支持BST的递归深度约10000
        int nums = 10000;
        System.out.println("AVL:"+testAVL(nums)+" s");
        //System.out.println("BST:"+testBST(nums)+" s");
    }

    private static double testAVL(int nums) {
        Random random = new Random();
        long startTime = System.nanoTime();
        AVLTree<Integer,Integer> avl = new AVLTree<>();
        for (int i = 0; i < nums; i++){
            avl.put(i,i);
        }
        System.out.println(avl.isBalanced());
        System.out.println(avl.isBinarySearchTree());
        /*  for (int i = 0; i < nums; i++){
            avl.containsKey(random.nextInt(nums));
        }*/
        for (int i = 0; i < nums; i++){
            avl.remove(i);
            if (!avl.isBalanced() || !avl.isBinarySearchTree()) {
                throw new RuntimeException("error");
            }
        }
        return (System.nanoTime() - startTime ) / 1000000000.0;
    }

    private static  double testBST(int nums) {
        Random random = new Random();
        /*Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums; i++){
            set.add(random.nextInt(nums));
        }*/
        long startTime = System.nanoTime();
        BSTMap<Integer,Integer> bstMap = new BSTMap<>();
        /*for (Integer i : set){
            bstMap.put(i,i);
        }*/
        for (int i = 0; i < nums; i++){
            bstMap.put(i,i);
        }
        for (int i = 0; i < nums; i++){
            bstMap.containsKey(random.nextInt(nums));
        }
        return (System.nanoTime() - startTime ) / 1000000000.0;
    }
}
