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

import org.mac.explorations.algs.ds.heap.MaximumHeap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 前 K 个高频元素
 *
 * @see {https://leetcode-cn.com/problems/top-k-frequent-elements/}
 *
 * @auther mac
 * @date 2019-11-03
 */
public class TopKFrequentElements {
    /**
     * 非空的整数数组，返回其中出现频率前 k 高的元素
     *
     * @param nums
     * @param k
     * @return
     */
    public List<Integer> of(int[] nums, int k) {
        Objects.requireNonNull(nums);
        if (k < 1) {
            throw new IllegalArgumentException("need k > 0");
        }
        // 整数频次统计
        Map<Integer,Integer> frequency = statisticalFrequency(nums);
        /**
         * 若不使用优先级队列可以使用最小堆或
         * 在实现堆的时候提供比较器或
         * 使用最大堆的时候重写元素的compareTo方法
         */
        //System.out.println(topKByHeap(frequency,k));
        return topK(frequency,k);
    }

    /**
     * 出现频率前 k 高的元素
     *
     * @param frequency
     * @param k
     * @return
     */
    private  LinkedList<Integer> topK(Map<Integer, Integer> frequency, int k) {

        PriorityQueue<FrequencyTuple> topK = new PriorityQueue<>();

        Set<Integer> keySet = frequency.keySet();
        for (Integer i : keySet) {

            if (topK.size() < k) {
                topK.enqueue(new FrequencyTuple(i,frequency.get(i)));
            }
            else if (topK.headElement().frequency < frequency.get(i)) {
                topK.dequeue();
                topK.enqueue(new FrequencyTuple(i,frequency.get(i)));
            }
        }

        LinkedList<Integer> topKInteger = new LinkedList<>();
        while (!topK.isEmpty()) {
            topKInteger.addFirst(topK.dequeue().element);
        }
        return topKInteger;
    }

    /**
     * 直接使用最大堆
     *
     * @param frequency
     * @param k
     * @return
     */
    private LinkedList<Integer>  topKByHeap(Map<Integer, Integer> frequency, int k) {

        MaximumHeap<FrequencyTuple> topK = new MaximumHeap<>();

        Set<Integer> keySet = frequency.keySet();
        for (Integer i : keySet) {
            if (topK.size() < k) {
                topK.add(new FrequencyTuple(i,frequency.get(i)));
            }
            else if (topK.max().frequency < frequency.get(i)) {
                topK.replace(new FrequencyTuple(i,frequency.get(i)));
            }
        }

        LinkedList<Integer> topKInteger = new LinkedList<>();
        while (!topK.isEmpty()) {
            topKInteger.addFirst(topK.take().element);
        }
        return topKInteger;
    }

    /**
     * 统计数组中整数的频次
     *
     * @param nums
     * @return
     */
    private Map<Integer, Integer> statisticalFrequency(int[] nums) {
        Map<Integer,Integer> frequency = new HashMap<>();
        for (int num : nums) {
            if (frequency.containsKey(num)) {
                frequency.put(num,frequency.get(num) + 1);
            }
            else {
                frequency.put(num,1);
            }
        }
        return frequency;
    }

    /**
     * 频次数据实体
     */
    private class FrequencyTuple implements Comparable<FrequencyTuple> {

        private final int element;
        private final int frequency;

        private FrequencyTuple(int element, int frequency) {
            this.element = element;
            this.frequency = frequency;
        }

        /**
         * 优先级队列使用的是最大堆实现
         * 当 frequency < another.frequency 返回 1
         * 即设定频次较小优先级越高,则元素入队后堆顶元素
         * 为队列中的最小元素
         *
         * @see org.mac.explorations.algs.ds.queue.PriorityQueue
         *
         * @param another
         * @return
         */
        @Override
        public int compareTo(FrequencyTuple another) {

            if ( frequency < another.frequency ) {
                return 1;
            }
            else if (frequency > another.frequency ){
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {1,1,1,2,2,3};
        int k = 2;

        System.out.println(new TopKFrequentElements().of(nums,k));
    }
}
