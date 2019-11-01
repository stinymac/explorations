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

package org.mac.explorations.algs.ds.set;

import java.util.Random;

/**
 * @auther mac
 * @date 2019-11-01
 */
public class Main {


    /**
     *            LinkedListSet                     BSTSet
     *
     * add         O(n)                             O(h)     O(log n)
     *
     * contains    O(n)                             O(h)     O(log n)
     *
     * remove      O(n)                             O(h)     O(log n)
     *
     * tip:
     *
     * 一颗满二分搜索树设level为树的层 树共有节点n：
     * level            nodes
     * 0                1   2^0
     * 1                2   2^1
     * 2                4   2^2
     * ...              ...
     * h-1              2^(h-1)
     *
     * 则 2^0 + 2^1 + 2^2......+ 2^(h-1) = n
     *
     * 1-2^h/1-2 = 2^h - 1 = n
     * 2^h = n+1
     * h = log(n+1)
     *
     * 因此一个二分树的平均时间复杂度为
     *
     * O(log n)
     *
     * 但最坏情况 二分树会退化为链表 时间复杂度为
     *
     * O(n) -> 数据过大时 由于使用递归 会出现栈溢出
     *
     * @param args
     */
    public static void main(String[] args) {

        Integer[] data = new Integer[100000];

        data[0] = 100000 / 2;
        Random random = new Random();

        for (int i = 1; i < 100000; i++) {
            data[i] = random.nextInt(100000);
        }

        // 此时二分树退化为链表
        /*for (int i = 0; i < 10000; i++) {
            data[i] = i;
        }*/


        Set<Integer> bstSet = new BSTSet<>();
        Set<Integer> linkedListSet = new LinkedListSet<>();


        System.out.println("linkedListSet:"+ test(linkedListSet,data));
        System.out.println("       bstSet:"+ test(bstSet,data));
    }

    private static <E> double test(Set<E> set,E[] data) {
        long startTime = System.nanoTime();
        for (E e : data) {
            set.add(e);
        }
        return (System.nanoTime() - startTime ) / 1000000000.0;
    }
}
