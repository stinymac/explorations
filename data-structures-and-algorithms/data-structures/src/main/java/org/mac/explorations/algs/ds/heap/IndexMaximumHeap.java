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

import java.util.NoSuchElementException;

/**
 * 索引最大堆
 *
 * @auther mac
 * @date 2019-11-02
 */
public class IndexMaximumHeap<E extends Comparable<E>> {

    private final DynamicArray<E> data;
    //堆中不保存数据而是保存数据的索引
    private final DynamicArray<Integer> indexes;
    //反向记录data[i]在索引堆数组中的索引
    private final DynamicArray<Integer> reverses;

    public IndexMaximumHeap() {
        this.data = new DynamicArray<>();
        indexes = new DynamicArray<>();
        reverses = new DynamicArray<>();
    }

    public IndexMaximumHeap(int capacity) {
        this.data = new DynamicArray<>(capacity);
        indexes = new DynamicArray<>(capacity);
        reverses = new DynamicArray<>(capacity);
    }

    public IndexMaximumHeap(E[] data) {
        this.data = new DynamicArray<>(data);
        int capacity = this.data.capacity();
        indexes = new DynamicArray<>(capacity);
        reverses = new DynamicArray<>(capacity);
        for (int i = 0; i < capacity; i++) {
            indexes.add(i,i);
            reverses.add(i,i);
        }

        int indexesSize = indexes.size();
        // 将索引数组堆化
        for (int i = parent( indexesSize -1); i >= 0; i--) {
            shiftDown(i);
        }
        /*
        for (int i = 0; i < indexesSize; i++) {
            int dataIndex = indexes.get(i);
            reverses.add(dataIndex,i);
        }*/
    }

    public int size() {
       return indexes.size();
    }

    public boolean isEmpty() {
        return indexes.isEmpty();
    }

    /**
     * data[i] 的索引i是否包含在堆中
     *
     * @param index
     * @return
     */
    private boolean containsDataIndex(int index) {
        return reverses.get(index) != null && indexes.get(reverses.get(index)) != null;
    }

    /**
     * 将data[i] 的值设为指定值
     *
     * @param index
     * @param element
     * @return
     */
    public E set(int index,E element) {
        // index 在索引堆中
        if (containsDataIndex(index)) {
            E ret = data.get(index);
            data.set(index,element);
            int heapIndex = reverses.get(index);
            shiftUp(heapIndex);
            shiftDown(heapIndex);
            return ret;
        }
        throw new NoSuchElementException("index not in heap");
    }

    public E get (int index) {
        if (containsDataIndex(index)) {
            return data.get(index);
        }
        throw new NoSuchElementException("index not in heap");
    }

    public void add (E e) {
        data.add(e);
        reverses.add(null);
        indexes.add(data.size() - 1);
        shiftUp(indexes.size() - 1);
    }

    /**
     * 添加节点后若需要则将新的节点上浮
     * 以满足二叉堆的性质
     *
     * @param index
     */
    private void shiftUp(int index) {

        while (index > 0
                && data.get(indexes.get(parent(index))).compareTo(data.get(indexes.get(index))) < 0) {

            int parent = parent(index);
            indexes.swap(index,parent);
            reverses.set(indexes.get(index),index);
            reverses.set(indexes.get(parent),parent);
            index = parent;
        }
        reverses.set(indexes.get(index),index);
    }

    /**
     * 堆顶元素
     *
     * @return
     */
    public E max() {
        return data.get(indexes.get(0));
    }

    /**
     * 取出堆顶元素
     *
     * @return
     */
    public E take () {

        if (indexes.isEmpty()) {
            throw new RuntimeException(" heap is empty");
        }
        E topElement = data.get(indexes.get(0));

        /**
         * 将堆最后一个元素交换到堆顶
         * 然后调整使其下沉以满足二叉堆的性质
         */
        int lastIndex = size()-1;
        indexes.swap(0,lastIndex);
        reverses.set(indexes.get(0),0);
        reverses.set(indexes.get(lastIndex),null);
        //只从indexes中删除 不从data中删除
        indexes.removeLast();

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

           if (data.get(indexes.get(index)).compareTo(data.get(indexes.get(largerChild))) < 0 ) {
               indexes.swap(index,largerChild);
               reverses.set(indexes.get(index),index);
               reverses.set(indexes.get(largerChild),largerChild);
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

        int left  = leftChild(index);// 一定有左子节点 - while (leftChild(index) < size)i
        int right = rightChild(index);
        // 右孩子索引不能越界
        if (right < size() && (data.get(indexes.get(left)).compareTo(data.get(indexes.get(right))) < 0)) {
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
        int maxElementIndex = indexes.get(0);
        data.set(maxElementIndex,e);
        shiftDown(0);
        return ret;
    }

    @Override
    public String toString() {
        return "    data:"+this.data.toString()+"\nreverses:"+this.reverses.toString()+"\n indexes:"+this.indexes.toString();
    }
}
