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
public class PlainUnionFind implements UnionFind{

    private final int[] id;

    public PlainUnionFind(int size) {

        this.id = new int[size];
        /**
         * 每一个元素分别在不同集合中
         */
        for (int i = 0; i < size; i++) {
            id[i] = i;
        }
    }

    @Override
    public void union(int p, int q) {

        int pId = find(p);
        int qId = find(q);

        if (pId == qId) {
            return;
        }

        for (int i = 0; i < id.length; i++) {
            if (pId == id[i]) {
                id[i] = qId;
            }
        }
    }

    /**
     * 元素p 和 q 是否在同一个集合
     *
     * @param p
     * @param q
     * @return
     */
    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    private int find (int p) {
        checkIndexRange(p);
        return id[p];
    }

    private void checkIndexRange(int index) {
        if (index < 0 || index >= id.length) {
            throw new IndexOutOfBoundsException("index bounds:[0,"+(id.length-1)+"]");
        }
    }

    @Override
    public int size() {
        return id.length;
    }
}
