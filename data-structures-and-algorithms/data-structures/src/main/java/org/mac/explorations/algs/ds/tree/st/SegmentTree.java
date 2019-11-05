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

package org.mac.explorations.algs.ds.tree.st;

import java.util.Arrays;
import java.util.Objects;

/**
 * 线段树
 *
 * 线段树的容量计算:
 *
 * 一颗满二分搜索树设level为树的层
 * level            nodes
 * 0                1   2^0
 * 1                2   2^1
 * 2                4   2^2
 * ...              ...
 * h-1              2^(h-1)
 *
 *
 *  0......h-2 共有  2^(h-1) - 1
 *
 * 则最后一层共有    2^(h-1)
 *
 * 对于个元素 要将其存储为线段树
 *
 * 若 n = 2^(h-1) :
 *
 * 则共需要 2^(h-1)  + 2^(h-1) -1  大约= 2n
 *
 * 若 n = 2^(h-1) + k (0 < k < 2^h):
 *
 * 则共需要 2^(h-1)  + 2^(h-1) -1 + 2^h = 2^(h-1)  + 2^(h-1) -1 + 2^(h-1)  + 2^(h-1) -1  大约= 4n
 *
 * @auther mac
 * @date 2019-11-03
 */
public class SegmentTree<E> {

    public interface Segmenter<E>{
        E merge(E le, E re);
    }

    private Object[] data;
    private Object[] tree;
    private Segmenter<E> segmenter;

    public SegmentTree(E[] data, Segmenter<E> segmenter) {

        Objects.requireNonNull(data);

        this.data = new Object[data.length];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i];
        }

        this.tree = new Object[data.length<<2];// 4n
        this.segmenter = segmenter;
        buildSegmentTree(0,0,data.length - 1);
    }

    /**
     * 创建index位置在区间[l...r]的线段树
     *
     * @param rootIndex
     * @param l
     * @param r
     */
    private void buildSegmentTree(int rootIndex,int l,int r) {

        if (l == r) {
            tree[rootIndex] = data[l];
            return;
        }

        int leftChildIndex = leftChild(rootIndex);
        int rightChildIndex = rightChild(rootIndex);

        //防止 l+r 溢出
        int mid = l + ( (r - l) >> 1) ;

        buildSegmentTree(leftChildIndex,l,mid);
        buildSegmentTree(rightChildIndex,mid + 1,r);

        tree[rootIndex] = segmenter.merge((E)tree[leftChildIndex],(E)tree[rightChildIndex]);
    }

    /**
     * 线段树中查询指定区间的分段合并值
     *
     * @param sor
     * @param eor
     * @return
     */
    public E query (int sor,int eor) {
        checkRange(sor,eor);
        return query(0,0,data.length-1,sor,eor);
    }

    /**
     * 从线段树中给定索引位置(其表示的段为[l...r])
     * 查询给定的段[sor,eor]的分段合并值
     *
     * @param treeIndex
     * @param l
     * @param r
     * @param sor
     * @param eor
     * @return
     */
    private E query(int treeIndex, int l, int r, int sor, int eor) {

        if (l == sor && r == eor) {
            return (E) tree[treeIndex];
        }

        int leftChildIndex = leftChild(treeIndex);
        int rightChildIndex = rightChild(treeIndex);

        int mid = l + ( (r - l) >> 1) ;

        if (eor <= mid) {
            return query(leftChildIndex, l, mid, sor,eor);
        }
        else if (mid + 1 <= sor) {
            return query(rightChildIndex, mid + 1, r, sor,eor);
        }

        E leftSegment = query(leftChildIndex, l, mid, sor,mid);
        E rightSegment = query(rightChildIndex, mid + 1, r, mid + 1,eor);

        return segmenter.merge(leftSegment,rightSegment);
    }

    /**
     * 检查区间值
     *
     * @param sor
     * @param eor
     */
    private void checkRange(int sor, int eor) {
        if (!(-1 < sor && sor < eor && eor < data.length)) {
            throw new IllegalArgumentException("range is error");
        }
    }

    /**
     * 获取指定索引元素
     *
     * @param index
     * @return
     */
    public E get(int index) {
        checkIndexRange(index);
        return (E)data[index];
    }

    /**
     * 将index位置元素更新为恶
     *
     * @param index
     * @param e
     */
    public void set(int index,E e) {
        checkIndexRange(index);
        data[index] = e;

        set( 0, 0, data.length - 1 , index,e);
    }

    /**
     * 递归寻找要更新的叶子节点
     * 并更新其关联的父节点分段
     *
     * @param treeIndex
     * @param l
     * @param r
     * @param index
     * @param e
     */
    private void set(int treeIndex,int l,int r,int index,E e){

        if (l == r) {
            tree[treeIndex] = e;
            return;
        }

        int mid = l + ( (r - l) >> 1) ;

        int leftChildIndex = leftChild(treeIndex);
        int rightChildIndex = rightChild(treeIndex);

        if (index <= mid) {
            set(leftChildIndex, l, mid , index,e);
        }
        else {
            set(rightChildIndex, mid + 1, r , index,e);
        }

        //递归更新其父节点分段
        tree[treeIndex] = segmenter.merge((E)tree[leftChildIndex],(E)tree[rightChildIndex]);
    }

    private void checkIndexRange(int index) {
        if (index < 0 || index >= data.length) {
            throw new IndexOutOfBoundsException("index bounds:[0,"+(data.length-1)+"]");
        }
    }

    /**
     * 真实的数据个数
     *
     * @return
     */
    public int size() {
        return data.length;
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

    @Override
    public String toString() {
        return Arrays.toString(tree);
    }
}
