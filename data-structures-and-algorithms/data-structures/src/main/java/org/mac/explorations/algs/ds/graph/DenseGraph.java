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

/**
 * 图：节点(Vertex)和边(Edge)组成的数学模型
 * 1、根据边是否有方向，分为有向图和无向图
 * 2、根据边是否有确定的值，分为有权图和无权图
 * 3、根据节点间是否有边连接，表示图的连通性
 * 4、边的两端连接同一个节点，称为自环边；两个节点间有多个边，这些边称为平行边（会加大算法的难度）
 * 5、没有自环边和平行边的图，称为简单图
 *
 * 图的表示方式：
 * 1、邻接矩阵(Adjacency Matrix)表示有向图和无向图
 * 2、邻接表(Adjacency List)表示有向图和无向图
 * 邻接表适合表示稀疏图（SparseGraph-边比较少的图）
 * 邻接矩阵适合表示稠密图（DenseGraph-边比较多的图：节点与其他几乎所有节点都有连线）
 * 完全图：单个节点与其他所有的节点都有连线
 *
 * 稠密图
 *
 * @auther mac
 * @date 2019-11-13
 */
public class DenseGraph {
    /**顶点数*/
    private int n;
    /**图边数*/
    private int m;

    /**是否是有向图*/
    private boolean directed;

    /**图的邻接矩阵*/
    private boolean[][] g;

    public DenseGraph(int n, boolean directed) {
        this.n = n;
        this.m = 0;
        this.directed = directed;
        g = new boolean[n][n];
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
        if (hasEdge(v,w)){
            return;
        }
        g[v][w] = true;
        if (!directed) {
            g[w][v] = true;
        }
        m++;
    }

    public boolean hasEdge(int v,int w) {
        checkRang(v,w);
        return g[v][w];
    }

    private void checkRang(int v, int w) {
        if ((v < 0 || v >= n) || (w < 0 || w >= n)){
            throw new IndexOutOfBoundsException("["+v+","+w+"] out of bounds");
        }
    }
}
