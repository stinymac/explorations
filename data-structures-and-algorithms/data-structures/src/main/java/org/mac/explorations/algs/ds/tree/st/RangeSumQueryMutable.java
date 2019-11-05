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

package org.mac.explorations.algs.ds.tree.st;

/**
 * 区域和检索 - 数组可修改
 *
 * 一个整数数组  nums，求出数组从索引 i 到 j  (i ≤ j) 范围内元素的总和，
 * 包含 i,  j 两点
 *
 * @see {https://leetcode-cn.com/problems/range-sum-query-mutable/}
 *
 * @auther mac
 * @date 2019-11-04
 */
public class RangeSumQueryMutable {

    private final SegmentTree<Integer> segmentTree;

    public RangeSumQueryMutable(Integer[] data) {

        this.segmentTree = new SegmentTree<>(data,(i, j) -> i+j);
    }

    public void update(int i, int val) {
        segmentTree.set(i,val);
    }

    public int sumRange(int i, int j) {
        return segmentTree.query(i,j);
    }

    public static void main(String[] args) {

        Integer[] nums = new Integer[]{1, 3, 5};
        RangeSumQueryMutable rsqm = new RangeSumQueryMutable(nums);

        System.out.println(rsqm.sumRange(0, 2));
        rsqm.update(1, 2);
        System.out.println(rsqm.sumRange(0, 2));
    }
}
