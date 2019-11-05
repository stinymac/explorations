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
 * 简单字符模式匹配
 *
 * @see {https://leetcode-cn.com/problems/add-and-search-word-data-structure-design/}
 *
 * 模式字符串只包含字母 . 或 a-z ( . 表示任何一个字母)
 *
 * @auther mac
 * @date 2019-11-05
 */
public class SimplePatternMatching {

    private static class Node {

        private boolean isWordEnding;
        private Map<Character,Node> next;

        public Node(boolean isWordEnding) {
            this.isWordEnding = isWordEnding;
            next = new HashMap<>();
        }

        public Node() {
            this(false);
        }
    }

    private Node root;

    public SimplePatternMatching() {
        root = new Node();
    }

    /**
     * 添加word构建字典
     *
     * @param word
     */
    private void add(String word) {

        Node p = root;
        int length = word.length();

        for (int i = 0; i < length ; i++) {
            char c = word.charAt(i);
            if (p.next.get(c) == null) {
                p.next.put(c,new Node());
            }
            p = p.next.get(c);
        }

        if (!p.isWordEnding) { // 单词最后一个字符
            p.isWordEnding = true;
        }
    }

    /**
     * 简单的模式匹配
     *
     * @param pattern
     * @return
     */
    public boolean match(String pattern) {
        Objects.requireNonNull(pattern);
        if (!pattern.matches("[\\.|(a-z)]+")){
            throw new UnsupportedOperationException("Just support:[\\\\.|(a-z)]+");
        }
        return match(root,pattern,0);
    }

    /**
     * 字典树中匹配
     *
     * @param p
     * @param pattern
     * @param index
     * @return
     */
    private boolean match(Node p, String pattern, int index) {

        if (index == pattern.length()) {
            return p.isWordEnding;
        }

        char c = pattern.charAt(index);
        if (c != '.') {
            if (p.next.get(c) == null) {
                return false;
            }
            return match(p.next.get(c),pattern,  index + 1);
        }
        else {
            /**
             * 如果pattern 是 . 则当前所有字符都匹配
             * 循环递归匹配所有字符的下个字符和模式的下个字符
             */
            for (char nextChar : p.next.keySet()) {
                if (match(p.next.get(nextChar),pattern,index + 1)) {
                    return true;
                }
            }
            // 循环递归匹配没有匹配的
            return false;
        }
    }

    public static void main(String[] args) {

        SimplePatternMatching spm = new SimplePatternMatching();
        spm.add("bad");
        spm.add("dad");
        spm.add("mad");

        System.out.println(spm.match("b.."));
    }
}
