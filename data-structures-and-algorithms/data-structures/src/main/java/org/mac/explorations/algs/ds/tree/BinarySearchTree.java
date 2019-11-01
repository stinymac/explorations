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

import org.mac.explorations.algs.ds.queue.LinkedListQueue;
import org.mac.explorations.algs.ds.queue.Queue;
import org.mac.explorations.algs.ds.stack.LinkedListStack;
import org.mac.explorations.algs.ds.stack.Stack;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 二分搜索树
 *
 * @auther mac
 * @date 2019-10-31
 */
public class BinarySearchTree<E extends Comparable<E>> {

    public static class Node<E> {

        private E element;
        private Node<E> left;
        private Node<E> right;
        private int childCount = 1;

        public Node(E element) {
            this.element = element;
            this.left = null;
            this.right = null;
        }

        public Node(E element, Node<E> left, Node<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }

        public E element() {
            return element;
        }

        public Node<E> left() {
            return left;
        }

        public Node<E> right() {
            return right;
        }
    }

    private Node<E> root;
    private int size;

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 向二分搜索树中插入节点
     *
     * @param element
     */
    public void add (E element) {
        root = add(root,element);
    }

    /**
     * 递归的将元素添加到正确的位置，并返回root节点
     *
     * @param root
     * @param element
     * @return
     */
    private Node<E> add(Node<E> root,E element) {
        //NULL 也可以看作为一棵树
        if (root == null) {
            size++;
            return new Node<>(element);
        }

        int resultId = element.compareTo(root.element);
        if (resultId < 0) {
            /**
             * 如果root的左子树的这root.left = new Node<>(element)
             * 否则 root.left = 左子树的根 即root.left
             */
            root.left = add(root.left,element);
            root.childCount = root.childCount + 1;
        }
        else if (resultId > 0) {
            root.right = add(root.right,element);
            root.childCount = root.childCount + 1;
        }
        return root;
    }

    /**
     * 元素查找
     *
     * @param element
     * @return
     */
    public boolean contains(E element) {
        return contains(this.root,element);
    }

    /**
     * 递归搜索元素
     *
     * @param root
     * @param element
     * @return
     */
    private boolean contains(Node<E> root, E element) {

        if (root == null) {
            return false;
        }

        int resultId = element.compareTo(root.element);
        if (resultId < 0) {
            return contains(root.left,element);
        }
        else if (resultId > 0) {
            return contains(root.right,element);
        }
        else {
            return true;
        }
    }

    public Node<E> root() {
        return root;
    }

    public int size () {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        return preorderTraversal(root)+"\n"+inorderTraversal(root)+"\n"+postorderTraversal(root);
    }

    /**
     * 树的前序遍历(先根遍历)
     *
     * @param root
     * @return
     */
    private String preorderTraversal(Node<E> root) {

        if (root == null) {
            return "";
        }

        return  root.element+"-"+root.childCount
                + " " + preorderTraversal(root.left)
                + " " + preorderTraversal(root.right);
    }

    /**
     * 树的前序遍历(先根遍历) - 非递归的方式
     *
     * @return
     */
    public void preorderTraversalNonRecursion() {

        Stack<Node<E>> stack = new LinkedListStack<>();
        stack.push(root);

        while (!stack.isEmpty()) {

            Node<E> current = stack.pop();
            System.out.print(current.element+" ");

            if (current.right != null) {
                stack.push(current.right);
            }
            if (current.left != null) {
                stack.push(current.left);
            }
        }

        System.out.println();
    }

    /**
     * 树的中序遍历
     *
     * @param root
     * @return
     */
    private String inorderTraversal(Node<E> root) {

        if (root == null) {
            return "";
        }

        return inorderTraversal(root.left)
                + " " + root.element +"-"+root.childCount
                + " " + inorderTraversal(root.right);
    }

    /**
     * 树的后序遍历
     *
     * @param root
     * @return
     */
    private String postorderTraversal(Node<E> root) {

        if (root == null) {
            return "";
        }

        return postorderTraversal(root.left)
                + " " + postorderTraversal(root.right)
                + " " + root.element+"-"+root.childCount;
    }


    /**
     * 树的层序遍历
     *
     * @return
     */
    public void levelorderTraversal() {

        Queue<Node<E>> queue = new LinkedListQueue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            Node<E> current = queue.dequeue();
            System.out.print(current.element+" ");

            if (current.left != null) {
                queue.enqueue(current.left);
            }
            if (current.right != null) {
                queue.enqueue(current.right);
            }
        }

        System.out.println();
    }

    /**
     * 二分搜索树的最小值(非递归方式)
     *
     * @return
     */
    public E min() {
        if (root == null){
            throw new NoSuchElementException("tree is null");
        }

        Node<E> p = root;
        while (p.left != null) {
            p = p.left;
        }
        return p.element;
    }

    /**
     * 递归定位最小值
     *
     * @param root
     * @return
     */
    private Node<E> min(Node<E> root) {

        if (root.left == null) {
            return root;
        }
        return min(root.left);
    }

    /**
     * 递归定位最大值
     *
     * @param root
     * @return
     */
    private Node<E> max(Node<E> root) {
        if (root.right == null) {
            return root;
        }
        return max(root.right);
    }

    /**
     * 二分搜索树的最大值(非递归方式)
     *
     * @return
     */
    public E max() {
        if (root == null){
            throw new NoSuchElementException("tree is null");
        }

        Node<E> p = root;
        while (p.right != null) {
            p = p.right;
        }
        return p.element;
    }

    /**
     * 删除二分搜索树的最小值
     *
     * @return
     */
    public E removeMin() {
        E element = min();
        root = removeMin(root);
        return element;
    }

    /**
     * 递归删除
     *
     * @param root
     * @return
     */
    private Node<E> removeMin(Node<E> root) {

        if (root.left == null) { // 当前节点为最小值
            //左节点为空，但是可能有右节点
            Node<E> rightChild = rightChildAfterRemoveMin(root);
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

    /**
     * 删除二分搜索树的最大值
     *
     * @return
     */
    public E removeMax() {
        E element = max();
        root = removeMax(root);
        return element;
    }

    /**
     * 递归删除
     *
     * @param root
     * @return
     */
    private Node<E> removeMax(Node<E> root) {

        if (root.right == null) { // 当前节点为最大值
            //右节点为空，但是可能有左节点
            Node<E> leftChild = leftChildAfterRemoveMax(root);
            // 返回左子树的根节点
            return leftChild;
        }
        /**
         * 右子树不为空，则当前节点的右子树
         * 指向其右子树删除最大值后的根节点
         */
        root.right = removeMax(root.right);
        return root;
    }


    /**
     * 删除二分搜索树的指定值
     *
     * @return
     */
    public void remove(E element) {
        root = remove(root,element);
    }

    private Node<E> remove(Node<E> root, E element) {

        if (root == null) {
            return root;
        }

        int resultId = element.compareTo(root.element);
        if (resultId < 0) {
            root.left = remove(root.left,element);
            return root;
        }
        else if (resultId > 0) {
            root.right = remove(root.right,element);
            return root;
        }
        else {// 删除节点

            if (root.left == null) {
                Node<E> rightChild = rightChildAfterRemoveMin(root);
                return rightChild;
            }
            if (root.right == null) {
                Node<E> leftChild = leftChildAfterRemoveMax(root);
                return leftChild;
            }
            /**
             * 节点的左右子树都不为空
             * 从该节点的右子树删除最小值,
             * 并将该最小值作为删除节点的后继节点
             */

            Node<E> successor = min(root.right);
            successor.right =  removeMin(root.right);
            successor.left = root.left;
            root.right = root.left = null;

            return successor;
        }
    }

    private Node<E> leftChildAfterRemoveMax(Node<E> node) {
        Node<E> leftChild = node.left;
        node.left = null;
        size--;
        return leftChild;
    }

    private Node<E>  rightChildAfterRemoveMin(Node<E> node) {
        Node<E> rightChild =  node.right;
        node.right = null;
        size--;
        return rightChild;
    }

    /**
     * 二分搜索树中小于给定元素的最大元素
     *
     * @param element
     * @return
     */
    public E floor(E element) {
        Objects.requireNonNull(element);
        return floor(root,element,0);
    }

    private E floor(Node<E> root, E element,int depth) {
        System.out.println("depth:"+depth+depthString(depth)+((root==null)?"NULL":root.element));
        if (root == null) {
            return null;
        }

        int resultId = element.compareTo(root.element);
        /**
         * 给定元素小于等于根元素
         * 则在根的左子树中寻找
         * 可能存在，也可能不存在(element < min(root.left))
         * 找到返回 找不到返回null
         */
        if (resultId <= 0) {
            E t = floor(root.left,element,depth+1);
            System.out.println("depth:"+depth+depthString(depth)+((t==null)?"NULL":t));
            return t;
        }
        /**
         * 给定元素大于root 则在root的右子树中寻找
         * 若没有找到当前root元素则为小于给定元素的最大元素值
         *
         */
        else  {
            E t = floor(root.right,element,depth+1);
            if (t != null) {
                System.out.println("depth:"+depth+depthString(depth)+((t==null)?"NULL":t));
                return t;
            }
            System.out.println("depth:"+depth+depthString(depth)+((root==null)?"NULL":root.element));
            return root.element;
        }
    }

    private String depthString(int depth) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i <= depth; i++) {
            res.append("-");
        }
        return res.toString();
    }

    /**
     * 二分搜索树中大于给定元素的最小元素
     *
     * @param element
     * @return
     */
    public E ceil(E element) {
        Objects.requireNonNull(element);
        return ceil(root,element);
    }

    private E ceil(Node<E> root, E element) {

        if (root == null) {
            return null;
        }

        int resultId = element.compareTo(root.element);

        if (resultId > 0) {
            return ceil(root.right,  element);
        }
        else {
            E r = ceil(root.left,  element);
            return r == null ? root.element : r;
        }
    }
}
