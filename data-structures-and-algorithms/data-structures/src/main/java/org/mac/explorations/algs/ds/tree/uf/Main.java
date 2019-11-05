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

package org.mac.explorations.algs.ds.tree.uf;

import java.util.Random;

/**
 * @auther mac
 * @date 2019-11-05
 */
public class Main {
    private static double testUF(UnionFind uf, int m){

        int size = uf.size();
        Random random = new Random();

        long startTime = System.nanoTime();


        for(int i = 0 ; i < m ; i ++){
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            uf.union(a, b);
        }

        for(int i = 0 ; i < m ; i ++){
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            uf.isConnected(a, b);
        }

        long endTime = System.nanoTime();

        return (endTime - startTime) / 1000000000.0;
    }

    public static void main(String[] args) {

        int size = 100000;
        int m = 100000;

        UnionFind uf1 = new PlainUnionFind(size);
        System.out.println("PlainUnionFind : " + testUF(uf1, m) + " s");


        UnionFind uf2 = new UtilityUnionFind(size);
        System.out.println("UtilityUnionFind : " + testUF(uf2, m) + " s");
    }
}
