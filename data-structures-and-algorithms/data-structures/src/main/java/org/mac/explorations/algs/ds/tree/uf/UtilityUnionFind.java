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

/**
 * @auther mac
 * @date 2019-11-05
 */
public class UtilityUnionFind implements UnionFind{

    private final int[] parent;
    /**
     * sz[i] 表示以i作为根的树的大小;
     */
    //private final int[] sz;

    /**
     * rank[i] 表示以i作为根的树的高度;
     */
    private final int[] rank;

    public UtilityUnionFind(int size) {

        this.parent = new int[size];
        //this.sz = new int[size];
        this.rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            //sz[i] = 1;
            rank[i] = 1;
        }
    }

    /**
     * 查找元素p
     *
     * @param p
     * @return
     */
    private int find(int p) {

        while (p != parent[p]) { //未到根节点
            // 路径压缩降低树的高度
            parent[p] = parent[parent[p]];
            //未到根节点 则parent[p] 表示的是其父节点的索引
            p = parent[p];
        }

        // version 2
        /**
        if (p != parent[p]) {
            parent[p] = find(parent[p]);
        }
        return parent[p];
        */

        return p;
    }

    @Override
    public void union(int p, int q) {

        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot)
            return;
        // version 1
        //parent[pRoot] = qRoot;

        /**
         * 判断树的高度,合并让高度小的子树直接指向高度大的树的根节点
         * (如果不这么做 在一定条件下 这样的方式耗时反而会大于PlainUnionFind)
         */
        // version 2
        /*
        if(sz[pRoot] < sz[qRoot]) {
            parent[pRoot] = qRoot;
            sz[qRoot] += sz[pRoot];
        }
        else {
            parent[qRoot] = pRoot;
            sz[pRoot] += sz[qRoot];
        }
        */

        // version 3
        if(rank[pRoot] < rank[qRoot]) {
            parent[pRoot] = qRoot;
        }
        else if (rank[pRoot] > rank[qRoot]){
            parent[qRoot] = pRoot;
        }
        else {
            parent[qRoot] = pRoot;
            rank[pRoot] += 1;
        }
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    @Override
    public int size() {
        return parent.length;
    }
}
