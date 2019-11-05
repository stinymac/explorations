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

package org.mac.explorations.algs.ds.tree.trie;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 键值映射
 *
 * 类里的两个方法，insert 和 sum。
 *
 * 对于方法 insert，输入一对（字符串，整数）的键值对。
 * 字符串表示键，整数表示值。如果键已经存在，
 * 那么原来的键值对将被替代成新的键值对。
 *
 * 对于方法 sum，输入一个表示前缀的字符串，
 * 返回所有以该前缀开头的键的值的总和。
 *
 * 示例:
 *
 * 输入: insert("apple", 3), 输出: Null
 * 输入: sum("ap"), 输出: 3
 * 输入: insert("app", 2), 输出: Null
 * 输入: sum("ap"), 输出: 5
 *
 * @see {https://leetcode-cn.com/problems/map-sum-pairs/}
 *
 * @auther mac
 * @date 2019-11-05
 */
public class MapSumPairs {

    private static class Node {

        private  int value;
        private Map<Character,Node> next;

        public Node(int value) {
            this.value = value;
            next = new HashMap<>();
        }

        public Node() {
            this(0);
        }
    }

    private Node root;

    public MapSumPairs() {
        root = new Node();
    }

    /**
     * 插入key 并以value值结束
     *
     * 相当于 key -> value
     *
     * @param key
     * @param val
     */
    public void insert(String key, int val) {

        Objects.requireNonNull(key);

        Node p = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            Node next = p.next.get(c);
            if (next == null) {
                p.next.put(c,new Node());
            }
            p = p.next.get(c);
        }

        p.value = val;
    }

    /**
     * 给定前缀的全部key的value的和
     *
     * @param prefix
     * @return
     */
    public int sum(String prefix) {
        Objects.requireNonNull(prefix);
        // 前缀匹配
        Node p = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (p.next.get(c) == null){
                return 0;
            }
            p = p.next.get(c);
        }
        /**
         * p 当前位于匹配的前缀的末尾处 即:
         * p -> lastChar.nextNode (value 值所在节点)
         *
         */
         return sum(p);
    }

    /**
     * 从给定节点递归求和
     *
     * @param p
     * @return
     */
    private int sum(Node p) {
        int sum = 0;
        if (p.next.isEmpty()){
            //当前位置本身的value
            sum += p.value;
        }
        for (char c : p.next.keySet()) {
            sum += sum(p.next.get(c));
        }
        return sum;
    }

    public static void main(String[] args) {
        MapSumPairs mapSumPairs = new MapSumPairs();
        mapSumPairs.insert("apple",3);
        System.out.println(mapSumPairs.sum("ap"));
        mapSumPairs.insert("app",2);
        System.out.println(mapSumPairs.sum("ap"));
    }
}
