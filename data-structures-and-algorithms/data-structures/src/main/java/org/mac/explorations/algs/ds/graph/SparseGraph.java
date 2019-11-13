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

import java.util.LinkedList;
import java.util.List;

/**
 * 稀疏图
 *
 * @auther mac
 * @date 2019-11-13
 */
public class SparseGraph {
    /**顶点数*/
    private int n;
    /**图边数*/
    private int m;

    /**是否是有向图*/
    private boolean directed;

    /**图的邻接表*/
    private List<Integer>[] g;

    public SparseGraph(int n, boolean directed) {
        this.n = n;
        this.m = 0;
        this.directed = directed;
        g = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            g[i] = new LinkedList<>();
        }
    }

    /**
     * 图的顶点数
     *
     * @return
     */
    public int vertexes() {
        return n;
    }

    /**
     * 图的边数
     *
     * @return
     */
    public int edges() {
        return m;
    }

    /**
     * 添加边
     *
     * @param v
     * @param w
     */
    public void addEdge(int v,int w) {
        checkRang(v,w);
        // 时间复杂度为O(n) 因此一般忽略平行边
        /*if (hasEdge(v,w)){
            return;
        }*/
        g[v].add(w);
        // v != w 排除自环边
        if (v != w && !directed) {
            g[w].add(v);
        }
        m++;
    }

    public boolean hasEdge(int v,int w) {
        checkRang(v,w);
        int size = g[v].size();
        for (int i = 0 ; i < size; i++) {
            if (g[v].get(i) == w) {
                return true;
            }
        }
        return false;
    }

    private void checkRang(int v, int w) {
        if ((v < 0 || v >= n) || (w < 0 || w >= n)){
            throw new IndexOutOfBoundsException("["+v+","+w+"] out of bounds");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append("node["+i+"]->"+g[i].toString()).append("\n");
        }
        return builder.toString();
    }
}
