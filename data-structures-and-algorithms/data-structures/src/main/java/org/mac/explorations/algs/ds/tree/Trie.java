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

package org.mac.explorations.algs.ds.tree;

import java.util.HashMap;
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
        int length = word.length();

        if (useRecursion) {
            addRecursively(root,word,0,length);
        }
        else {
            addIteratively(word, length);
        }
    }

    /**
     * 非递归添加
     *
     * @param word
     * @param length
     */
    private void addIteratively(String word, int length) {

        Node p = root = (root == null ? new Node() : root);

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
        PostAddProcessing(p);
    }

    /**
     * 单词添加结束后处理
     *
     * @param p
     */
    private void PostAddProcessing(Node p) {
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
    private void addRecursively(Node root,String word,int charAtIndex,int length) {

        if (charAtIndex == length) {
            return;
        }

        char c = word.charAt(charAtIndex);
        Node p = add(root, c);

        if (charAtIndex == 0)  {
            this.root = p;
        }
        int nextIndex = charAtIndex + 1;
        addRecursively(p.next.get(c),word,nextIndex,length);

        if (charAtIndex == length - 1) {
            PostAddProcessing(p);
            return;
        }
    }

    private Node add(Node root, char c) {

        if (root == null) {
            Node newNode = new Node();
            newNode.next.put(c,new Node());
            return newNode;
        }

        Node next = root.next.get(c);

        if (next == null) {
            root.next.put(c, new Node());
        }

        return root;
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
