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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Map;

/**
 * 英文单词 Trie
 *
 * @auther mac
 * @date 2019-11-04
 */
public class Trie {

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
    private int size;

    public Trie() {
        root = new Node();
    }

    /**
     * 默认不使用递归添加
     *
     * @param word
     */
    public void add (String word) {
        add (word,false);
    }
    /**
     * 添加单词
     *
     * @param word
     * @param useRecursion
     */
    public void add (String word,boolean useRecursion) {

        Objects.requireNonNull(word);

        if (useRecursion) {
            addRecursively(root,word,0);
        }
        else {
            addIteratively(word);
        }
    }

    /**
     * 非递归添加
     *
     * @param word
     */
    private void addIteratively(String word) {

        Node p = root;
        int length = word.length();

        for (int i = 0; i < length ; i++) {

           char c = word.charAt(i);
           Node next = p.next.get(c);

           if (next == null) {
               Node newNode = new Node();
               p.next.put(c,newNode);
               next = newNode;
           }

           p = next;
        }
        postAddProcessing(p);
    }

    /**
     * 单词添加结束后处理
     *
     * @param p
     */
    private void postAddProcessing(Node p) {
        if (!p.isWordEnding) { // 单词最后一个字符
            p.isWordEnding = true;
            size++;
        }
    }

    /**
     * 递归添加
     *
     * @param word
     */
    private void addRecursively(Node root,String word,int charAtIndex) {

        if (charAtIndex == word.length()) {
            postAddProcessing(root);
            return;
        }

        char c = word.charAt(charAtIndex);
        if (root.next.get(c) == null) {
            root.next.put(c,new Node());
        }

        int nextIndex = charAtIndex + 1;
        addRecursively(root.next.get(c),word,nextIndex);
    }

    /**
     * 单词查询
     *
     * @param word
     * @return
     */
    public boolean contains(String word) {

        Node p = root;

        int length = word.length();
        for (int i = 0; i < length ; i++) {

            char c = word.charAt(i);
            Node next = p.next.get(c);

            if (next == null) {
               return false;
            }

            p = next;
        }

        return p.isWordEnding;
    }

    /**
     * 单词是否是一个前缀
     *
     * @param word
     * @return
     */
    public boolean isPrefix(String word) {

        Node p = root;

        int length = word.length();
        for (int i = 0; i < length ; i++) {

            char c = word.charAt(i);
            if (p.next.get(c) == null) {
                return false;
            }
            p = p.next.get(c);
        }

        return true;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ");
        builder.append("size:"+size+"->[  ");
        buildString(root,builder);
        builder.append(" ] ");
        builder.append("}");
        return builder.toString();
    }

    private void buildString(Node root, StringBuilder builder) {

        if(root == null || root.next == null || root.next.size() <= 0) {
            builder.append(" ");
            return;
        }

        Map<Character,Node> next = root.next;

        for (Character c:next.keySet()) {
            builder.append(c);
            buildString(next.get(c),  builder);
        }
    }
}
