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

package org.mac.explorations.algs.ds.map;

import java.util.NoSuchElementException;

/**
 * 二分树实现Map
 *
 * @auther mac
 * @date 2019-11-02
 */
public class BSTMap<K extends Comparable<K>,V> implements Map<K,V> {

    public static class Entry<K extends Comparable<K>,V> implements Map.Entry<K,V> {

        private K key;
        private V value;

        private Entry<K ,V> left;
        private Entry<K ,V> right;

        public Entry(K key, V value, Entry<K, V> left, Entry<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }

        public boolean equals (Object obj) {
            if (obj instanceof Entry) {
                Entry<K,V> other = (Entry) obj;
                return key.equals(other.key) && value.equals(other.key);
            }
            return false;
        }
    }

    private Entry<K ,V> root;
    private int size;


    /**
     * 在树中定位给定的Key
     *
     * @param key
     * @return
     */
    private Entry<K ,V> position(Entry<K ,V> root,K key) {

        if (root == null) {
            return null;
        }

        int c = key.compareTo(root.key);

        if (c < 0) {
            return position(root.left,key);
        }
        else if (c > 0) {
            return position(root.right,key);
        }
        else {
            return root;
        }
    }

    @Override
    public V put(K key, V value) {

        Entry<K, V> newNode = new Entry<>(key,value,null,null);
        root =  putEntry(root,newNode);
        return value;
    }

    private Entry<K, V> putEntry(Entry<K, V> root, Entry<K, V> kvEntry) {

        if (root == null) {
            size++;
            return kvEntry;
        }

        int c = kvEntry.key.compareTo(root.key);

        if (c < 0) {
            root.left = putEntry(root.left,kvEntry);
        }
        else if (c > 0) {
            root.right =  putEntry(root.right,kvEntry);
        }
        else { // 重复的Key
            root.setValue(kvEntry.value);
        }

        return root;
    }

    @Override
    public V remove(K key) {
        root = remove(root, key);
        return null;
    }

    private  Entry<K, V> remove( Entry<K, V> root, K key) {

        if (root == null) {
            return root;
        }

        int resultId = key.compareTo(root.key);
        if (resultId < 0) {
            root.left = remove(root.left, key);
            return root;
        } else if (resultId > 0) {
            root.right = remove(root.right, key);
            return root;
        } else {// 删除节点

            if (root.left == null) {
                Entry<K, V> rightChild = rightChildAfterRemoveMin(root);
                return rightChild;
            }
            if (root.right == null) {
                Entry<K, V> leftChild = leftChildAfterRemoveMax(root);
                return leftChild;
            }
            /**
             * 节点的左右子树都不为空
             * 从该节点的右子树删除最小值,
             * 并将该最小值作为删除节点的后继节点
             */

            Entry<K, V> successor = min(root.right);
            successor.right = removeMin(root.right);
            successor.left = root.left;
            root.right = root.left = null;

            return successor;
        }
    }

    private Entry<K, V> min(Entry<K, V> root) {

        if (root.left == null) {
            return root;
        }
        return min(root.left);
    }

    private Entry<K, V>  removeMin(Entry<K, V>  root) {

        if (root.left == null) { // 当前节点为最小值
            //左节点为空，但是可能有右节点
            Entry<K, V>  rightChild = rightChildAfterRemoveMin(root);
            // 返回右子树的根节点
            return rightChild;
        }
        /**
         * 左子树不为空，则当前节点的左子树
         * 指向其左子树删除最小值后的根节点
         */
        root.left = removeMin(root.left);
        return root;
    }


    private Entry<K, V>  leftChildAfterRemoveMax(Entry<K, V>  node) {
        Entry<K, V>  leftChild = node.left;
        node.left = null;
        size--;
        return leftChild;
    }

    private Entry<K, V>   rightChildAfterRemoveMin(Entry<K, V>  node) {
        Entry<K, V>  rightChild =  node.right;
        node.right = null;
        size--;
        return rightChild;
    }

    @Override
    public V get(K key) {

        Entry<K, V> p = position(root,key);

        if (p == null ) {
            throw new NoSuchElementException("Not exist key:"+key);
        }

        return p.value;
    }

    /**
     * 时间复杂度 O(log n) -> O(n)
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(K key) {
        return position(root,key) != null;
    }

    /**
     * 时间复杂度 O(n)
     *
     * @param value
     * @return
     */
    @Override
    public boolean containsValue(V value) {
        return containsValue(root,value);
    }

    private boolean containsValue(Entry<K, V> root, V value) {

        if (root == null) {
            return false;
        }

        boolean l = containsValue(root.left,value);
        if (l) {
            return true;
        }
        if ((value == null && root.value == null) || value.equals(root.value)) {
            return true;
        }
        boolean r = containsValue(root.right,value);
        return r;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString(root,builder);
        return builder.toString();
    }

    private void toString(Entry<K, V> root, StringBuilder builder) {

        if (root == null){
            builder.append("");
            return;
        }

        toString(root.left, builder);
        builder.append("["+root.key+":"+root.value+"]->");
        toString(root.right, builder);
    }
}
