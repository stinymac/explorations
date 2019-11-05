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

import java.util.Objects;

/**
 * 区域和检索 - 数组不可变
 *
 * @see {https://leetcode-cn.com/problems/range-sum-query-immutable/}
 *
 * @auther mac
 * @date 2019-11-04
 */
public class RangeSumQueryImmutable {

    private static class WithSegmentTree {
        private final SegmentTree<Integer> segmentTree;

        public WithSegmentTree(Integer[] data) {
            this.segmentTree = new SegmentTree<>(data,(i, j) -> i+j);
        }

        public int sumRange(int i,int j) {
            return segmentTree.query(i,j);
        }
    }

    private static class WithPretreatment {
        /**
         * sum[i]用以保存区间[0...i-1]的值 即:
         *
         * sum[0] = 0;
         * sum[1] = nums[0]
         * ......
         * sum[i] = nums[0]+nums[0]...nums[i-1]
         *
         */
        private final int[] sum;

        private WithPretreatment(int[] nums) {
            Objects.requireNonNull(nums);
            this.sum = new int[nums.length + 1];
            sum[0] = 0;
            for (int i = 1; i < sum.length; i++) {
                sum[i] = sum[i - 1] + nums[i - 1];
            }
        }

        public int sumRange(int i, int j) {

            return sum[j+1] - sum [i];
        }
    }

    public static void main(String[] args) {
        Integer[] data = new Integer[]{-2, 0, 3, -5, 2, -1};
        WithSegmentTree rsq = new WithSegmentTree(data);

        System.out.println(rsq.sumRange(0,2));
        System.out.println(rsq.sumRange(2,5));

        int[] nums = new int[]{-2, 0, 3, -5, 2, -1};
        WithPretreatment p = new WithPretreatment(nums);
        System.out.println(p.sumRange(0,2));
        System.out.println(p.sumRange(2,5));
        System.out.println(p.sumRange(0,5));
    }
}
