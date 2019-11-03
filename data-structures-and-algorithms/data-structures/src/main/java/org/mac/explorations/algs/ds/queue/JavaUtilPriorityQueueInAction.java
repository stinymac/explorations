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

package org.mac.explorations.algs.ds.queue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 使用Java的优先级队列(最小堆)实现
 *
 * 前 K 个高频元素
 *
 * @see {https://leetcode-cn.com/problems/top-k-frequent-elements/}
 *
 * @auther mac
 * @date 2019-11-03
 */
public class JavaUtilPriorityQueueInAction {

    public List<Integer> topK(int[] nums, int k) {

        final Map<Integer,Integer> frequency = new HashMap<>();

        for (int num : nums) {
            if (frequency.containsKey(num)) {
                frequency.put(num,frequency.get(num) + 1);
            }
            else {
                frequency.put(num,1);
            }
        }

        PriorityQueue<Integer> topK = new PriorityQueue<>(Comparator.comparingInt(frequency::get));

        Set<Integer> keySet = frequency.keySet();
        for (Integer i : keySet) {
            if (topK.size() < k) {
                topK.add(i);
            }
            else if (frequency.get(topK.peek()) < frequency.get(i)) {
                topK.remove();
                topK.add(i);
            }
        }

        List<Integer> topKInteger = new ArrayList<>(k);
        while (!topK.isEmpty()) {
            topKInteger.add(topK.remove());
        }
        return topKInteger;
    }

    public static void main(String[] args) {
        int[] nums = {1,1,1,2,2,3};
        int k = 2;

        System.out.println(new JavaUtilPriorityQueueInAction().topK(nums,k));
    }
}
