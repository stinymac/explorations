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

package org.mac.explorations.algs.ds.tree.avl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 平衡二叉树:
 *
 * 任意节点的左右子树的高度差不超过1
 *
 * @auther mac
 * @date 2019-11-06
 */
public class AVLTree <K extends Comparable<K>,V> {

    public static class Node<K extends Comparable<K>,V> {

        private K key;
        private V value;

        private Node<K ,V> left;
        private Node<K ,V> right;

        private int height;

        public Node(K key, V value, Node<K, V> left, Node<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public Node(K key, V value, Node<K, V> left, Node<K, V> right,int height) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = height;
        }

        public V setValue(V value) {
            return this.value = value;
        }
    }

    private Node<K,V> root;
    private int size;

    /**
     * 在树中定位给定的Key
     *
     * @param key
     * @return
     */
    private Node<K, V> position(Node<K, V> root, K key) {

        while (true) {

            if (root == null) {
                return null;
            }

            int c = key.compareTo(root.key);

            if (c > 0) {
                root = root.right;
            } else if (c < 0) {
                root = root.left;
            } else {
                return root;
            }
        }
    }

    /**
     * 节点高度
     *
     * @param node
     * @return
     */
    private int height(Node node) {
        if (node == null){
            return 0;
        }
        return node.height;
    }

    /**
     * 计算节点平衡因子
     *
     * @param node
     * @return
     */
    private int calBalanceFactor(Node node) {

        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    /**
     * 判断是否满足二分搜索树
     *
     * @return
     */
    public boolean isBinarySearchTree () {
        List<K> keys = new ArrayList<>(this.size);
        inOrder(root,keys);

        for (int i = 1; i < size; i++) {
            if (keys.get(i-1).compareTo(keys.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    private void inOrder(Node<K, V> root, List<K> keys) {
        if (root == null){
            return;
        }

        inOrder(root.left, keys);
        keys.add(root.key);
        inOrder(root.right, keys);
    }

    /**
     * 判断是否平衡
     *
     * @return
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node<K, V> root) {
        if (root == null) {
            return true;
        }

        if (calBalanceFactor(root) > 1) {
            return false;
        }

        return isBalanced(root.left) && isBalanced(root.right);
    }

    public V put(K key, V value) {

        Node<K, V> newNode = new Node<>(key,value,null,null);
        root =  putNode(root,newNode);
        return value;
    }

    /**
     * 二分搜索树插入节点后其根节点或
     * 祖先节点的平衡可能被打破
     *
     *
     *
     * @param root
     * @param kvNode
     * @return
     */
    private Node<K, V> putNode(Node<K, V> root, Node<K, V> kvNode) {

        if (root == null) {
            size++;
            return kvNode;
        }

        int c = kvNode.key.compareTo(root.key);

        if (c < 0) {
            root.left = putNode(root.left,kvNode);
        }
        else if (c > 0) {
            root.right =  putNode(root.right,kvNode);
        }
        else { // 重复的Key
            root.setValue(kvNode.value);
        }

        root.height = 1 + Math.max(height(root.left),height(root.right));

        return balanceIfNeeded(root);
    }

    /**
     * 以向给定根节点的左子树节点的左节点添加节点为例说明一次添加后的平衡维护:
     *
     * 添加一个节点后(向最左边添加),指定根的树平衡打破，
     * 根的左子树高度+1，若要是指平衡器左字数的高度需要-1
     *
     * 抽象为一般模型如下:
     *
     *               x
     *              / \
     *             y  T4
     *            / \
     *           z  T3
     *          / \
     *         T1 T2
     *
     *  设z的子树的高度为h  因为添加一个节点后以x为根的树变为不平衡
     *  x是第一个因添加而打破平衡的树 除去x 那么x的子树都是平衡的
     *
     *  height(z) = h+1
     *  height(T3) = h+1 或 h
     *  height(y) = h+2
     *
     *  平衡因高度+1打破则
     *
     *  height(T4) = h
     *
     *  由二分搜索树得:
     *  T1 < z < T2 < y < T3 < x < T4
     *
     *  向右旋转
     *
     *              y
     *            /   \
     *           z     x
     *          / \   / \
     *         T1 T2 T3 T4
     *
     * 旋转后 T1 < z < T2 < y < T3 < x < T4 还是满足的
     *
     * 旋转后  height(T3) = h+1 或 h  height(T4) = h 满足平衡
     *
     * height(x) = h+2 或  h+1
     * height(z) = h+1
     *
     * 因此 y 满足平衡
     *
     * @param root
     * @return
     */
    private Node<K, V> balanceIfNeeded(Node<K, V> root) {
        // 在左节点的某个子节点的左侧添加 LL
        if (calBalanceFactor(root) > 1 && calBalanceFactor(root.left) >= 0) {  // root.left.height - root.right.height
            return rightRotate(root);
        }
        // 在右节点的某个子节点的右侧添加 RR
        if (calBalanceFactor(root) < -1 && calBalanceFactor(root.right) <= 0) {
            return leftRotate(root);
        }
        // 在左节点的某个子节点的右侧添加 LR -> LL
        /**
         *        x
         *       / \                     x
         *      y   T4                  / \
         *     / \        ---->        z  T4   --->
         *    T3  z                   / \
         *       / \                 y   T2
         *      T1 T2               / \
         *                         T3 T1
         */
        if (calBalanceFactor(root) > 1 && calBalanceFactor(root.left) < 0) {
            root.left = leftRotate(root.left);
            return  rightRotate(root);
        }
        // 在右节点的某个子节点的左侧添加 RL -> RR
        /**
         *               x                    x
         *              / \                  / \
         *            T4   y       ---->   T4   z
         *               /  \                  / \
         *              z   T3                T1  y
         *             / \                       /\
         *            T1 T2                    T2 T3
         */
        if (calBalanceFactor(root) < -1 && calBalanceFactor(root.right) > 0) {
            root.right = rightRotate(root.right);
            return  leftRotate(root);
        }

        return root;
    }


    /**
     * 在平衡被打破的根节点右旋转维护平衡
     *             x                       y
     *            / \                    /   \
     * newRoot<- y  T4  ------->        z     x
     *          / \                    / \   / \
     *         z  T3  <-newRootRight  T1 T2 T3 T4
     *        / \
     *      T1  T2
     *
     * @param oldRoot
     * @return
     */
    private Node<K,V> rightRotate(Node<K, V> oldRoot) {

        Node<K, V> newRoot = oldRoot.left;
        Node<K, V> newRootRight = newRoot.right;

        newRoot.right = oldRoot;
        oldRoot.left = newRootRight;

        oldRoot.height = 1 + Math.max(height(oldRoot.left),height(oldRoot.right));
        newRoot.height = 1 + Math.max(height(newRoot.left),height(newRoot.right));

        return newRoot;
    }

    /**
     * 在平衡被打破的根节点左旋转维护平衡
     *          x
     *         / \                            y
     *       T4   y  ----------------->     /  \
     *          /  \                      x     z
     *        T3    z                    / \   / \
     *             / \                  T4 T3 T1 T2
     *            T1 T2
     *
     *
     * @param oldRoot
     * @return
     */
    private Node<K,V> leftRotate(Node<K, V> oldRoot) {

        Node<K, V> newRoot = oldRoot.right;
        Node<K, V> newRootLeft = newRoot.left;

        newRoot.left = oldRoot;
        oldRoot.right = newRootLeft;

        oldRoot.height = 1 + Math.max(height(oldRoot.left),height(oldRoot.right));
        newRoot.height = 1 + Math.max(height(newRoot.left),height(newRoot.right));

        return newRoot;
    }

    public V remove(K key) {
        root = remove(root, key);
        return null;
    }

    private  Node<K, V> remove( Node<K, V> root, K key) {

        if (root == null) {
            return root;
        }

        int resultId = key.compareTo(root.key);
        Node<K, V> retNode;
        if (resultId < 0) {
            root.left = remove(root.left, key);
            retNode =  root;
        } else if (resultId > 0) {
            root.right = remove(root.right, key);
            retNode =  root;
        } else {// 删除节点

            if (root.left == null) {
                retNode =  rightChildAfterRemoveMin(root);
            } else if (root.right == null) {
                retNode = leftChildAfterRemoveMax(root);
            } else {
                /**
                 * 节点的左右子树都不为空
                 * 从该节点的右子树删除最小值,
                 * 并将该最小值作为删除节点的后继节点
                 */

                Node<K, V> successor = min(root.right);
                successor.right = remove(root.right,successor.key);
                successor.left = root.left;
                root.right = root.left = null;
                retNode = successor;
            }
        }

        if(retNode == null) {
            return null;
        }

        root.height = 1 + Math.max(height(root.left),height(root.right));
        return balanceIfNeeded(retNode);
    }

    private Node<K, V> min(Node<K, V> root) {

        if (root.left == null) {
            return root;
        }
        return min(root.left);
    }

    private Node<K, V>  leftChildAfterRemoveMax(Node<K, V>  node) {
        Node<K, V>  leftChild = node.left;
        node.left = null;
        size--;
        return leftChild;
    }

    private Node<K, V>   rightChildAfterRemoveMin(Node<K, V>  node) {
        Node<K, V>  rightChild =  node.right;
        node.right = null;
        size--;
        return rightChild;
    }


    public V get(K key) {

        Node<K, V> p = position(root,key);

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

    public boolean containsKey(K key) {
        return position(root,key) != null;
    }

    /**
     * 时间复杂度 O(n)
     *
     * @param value
     * @return
     */
    public boolean containsValue(V value) {
        return containsValue(root,value);
    }

    private boolean containsValue(Node<K, V> root, V value) {

        if (root == null) {
            return false;
        }

        if (containsValue(root.left,value)) {
            return true;
        }
        if ((value == null && root.value == null) || value.equals(root.value)) {
            return true;
        }
        return containsValue(root.right,value);
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString(root,builder);
        return builder.toString();
    }

    private void toString(Node<K, V> root, StringBuilder builder) {

        if (root == null){
            builder.append("");
            return;
        }

        toString(root.left, builder);
        builder.append("["+root.key+":"+root.value+"]->");
        toString(root.right, builder);
    }
}
