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

package org.mac.explorations.algs.ds.graph;

import java.util.Random;

/**
 * @auther mac
 * @date 2019-11-13
 */
public class Main {

    public static void main(String[] args) {
        int n = 20;
        int m = 100;

        SparseGraph g = new SparseGraph(n,false);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0 ; i < m; i ++) {
           int v = random.nextInt(n);
           int w = random.nextInt(n);
           g.addEdge(v,w);
        }
        System.out.println(g);
    }
}
