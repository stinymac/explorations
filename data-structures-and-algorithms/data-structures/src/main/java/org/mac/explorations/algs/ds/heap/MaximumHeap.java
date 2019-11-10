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

package org.mac.explorations.algs.ds.heap;

import org.mac.explorations.algs.ds.array.DynamicArray;

/**
 * 满二叉树：
 * 一棵深度为k，且有2^k-1个节点的树是满二叉树。
 *
 * 完全二叉树：
 *
 * 若设二叉树的深度为h，除第 h 层外，其它各层 (1～h-1) 的结点数都达到最大个数，第h 层所有的结点都连续集中在最左边，这就是完全二叉树。
 * 满二叉树一定是完全二叉树，完全二叉树不一定是满二叉树。
 *
 * 二叉堆：
 *
 * 堆中节点的值总是不大于其父亲节点的最大值
 * 最大堆（相应的可以定义最小堆）
 *
 * 二叉堆索引规律：
 *
 *  根节点为1的情况
 *
 *  parent = index / 2
 *  leftChild = 2 * index
 *  rightChild= 2 * index + 1
 *
 *  根节点如果是0，则索引计算有相应的偏移
 *  parent = ( index -1 )/ 2
 *  leftChild = 2 * index + 1
 *  rightChild= 2 * index + 2
 *
 * @auther mac
 * @date 2019-11-02
 */
public class MaximumHeap<E extends Comparable<E>> {

    private final DynamicArray<E> data;

    public MaximumHeap() {
        this.data = new DynamicArray<>();
    }

    public MaximumHeap(int capacity) {
        this.data = new DynamicArray<>(capacity);
    }

    public MaximumHeap(E[] data) {
        this.data = new DynamicArray<>(data);
        // 将数组堆化
        for (int i = parent( this.data.size() -1); i >= 0; i--) {
            shiftDown(i);
        }
    }

    public int size() {
       return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void add (E e) {
        data.add(e);
        shiftUp(data.size() - 1);
    }

    /**
     * 添加节点后若需要则将新的节点上浮
     * 以满足二叉堆的性质
     *
     * @param index
     */
    private void shiftUp(int index) {

        while (index > 0
                && data.get(parent(index)).compareTo(data.get(index)) < 0) {

            int parent = parent(index);
            data.swap(index,parent);
            index = parent;
        }
    }

    /**
     * 堆顶元素
     *
     * @return
     */
    public E max() {
        return data.get(0);
    }

    /**
     * 取出堆顶元素
     *
     * @return
     */
    public E take () {
        if (data.isEmpty()) {
            throw new RuntimeException(" heap is empty");
        }
        E topElement = data.get(0);

        /**
         * 将堆最后一个元素交换到堆顶
         * 然后调整使其下沉以满足二叉堆的性质
         */
        data.swap(0,size()-1);
        data.removeLast();

        shiftDown(0);

        return topElement;
    }

    /**
     * 元素调整下沉
     *
     * @param index
     */
    private void shiftDown(int index) {

        /**
         * 给定索引的左孩子没有越界则需继续检查调整
         * 以满足二叉堆的性质
         */
        int size = size();
        while (leftChild(index) < size) {

           int largerChild =  indexOfLargerChild(index);

           if (data.get(index).compareTo(data.get(largerChild)) < 0 ) {
               data.swap(index,largerChild);
               index = largerChild;
           }
           else {
               break;
           }
        }
    }

    /**
     * 节点的子节点中较大节点的索引
     *
     * @param index
     * @return
     */
    private int indexOfLargerChild(int index) {

        int left = leftChild(index);// 一定有左子节点 - while (leftChild(index) < size)i
        int right = left + 1;
        if (right < size() && (data.get(left).compareTo(data.get(right)) < 0)) {
            return right;
        }
        return left;
    }

    /**
     * 指定节点的父节点索引
     *
     * @param index
     * @return
     */
    private int parent(int index){

        if (index == 0) {
            throw new IllegalArgumentException("index = 0 is root");
        }

        return ( index - 1 ) >> 1;
    }

    /**
     * 指定节点的左孩子节点索引
     *
     * @param index
     * @return
     */
    private int leftChild (int index) { // index > 0
        return (index << 1) + 1;
    }


    /**
     * 指定节点的右孩子节点索引
     *
     * @param index
     * @return
     */
    private int rightChild (int index) { // index > 0
        return (index << 1) + 2;
    }

    /**
     * 用给定元素替换堆顶元素
     * 并调整新元素的位置使之满足二叉堆的性质
     *
     * @param e
     * @return
     */
    public E replace(E e) {
        E ret = max();
        data.set(0,e);
        shiftDown(0);
        return ret;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
