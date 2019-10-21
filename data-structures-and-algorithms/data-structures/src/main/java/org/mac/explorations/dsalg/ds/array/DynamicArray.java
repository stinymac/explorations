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

package org.mac.explorations.dsalg.ds.array;

/**
 * 动态数组
 *
 * @see java.util.ArrayList
 *
 * @auther mac
 * @date 2019-10-21
 */
public class DynamicArray<E> {

    /**
     * 存放数据的数组
     */
    private transient Object[] elementData;

    /**
     * 数组中元素的个数
     */
    private int size;

    /**
     * 数组默认的初始化容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 共享的空数组
     */
    private static final Object[] EMPTY_ELEMENT_DATA = {};

    /**
     * 默认的空容量的数组
     *
     * initialCapacity == 0 使用 EMPTY_ELEMENT_DATA
     * 未指定initialCapacity 使用 DEFAULT_EMPTY_CAPACITY_ELEMENT_DATA
     */
    private static final Object[] DEFAULT_INIT_EMPTY_CAPACITY_ELEMENT_DATA = {};

    /**
     * 默认构造
     */
    public DynamicArray () {
        this.elementData = DEFAULT_INIT_EMPTY_CAPACITY_ELEMENT_DATA ;
    }

    /**
     * 指定数组容量的构造器
     *
     * @param initialCapacity
     */
    public DynamicArray (int initialCapacity) {

        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity:" + initialCapacity + " is illegal.");
        }
        else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENT_DATA;
        }
        else {
            this.elementData = new Object[initialCapacity];
        }
    }

    /**
     * 数组元素
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 数组容量
     *
     * @return
     */
    public int capacity() {
        return this.elementData.length;
    }

    /**
     * 数组是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 向数数组头添加元素
     *
     * @param e
     */
    public void addFirst (E e) {
        add(0,e);
    }
    /**
     * 向数数组末尾添加元素
     *
     * @param e
     */
    public void add (E e) {
        add(size,e);
    }

    /**
     * 向数组的指定位置添加元素
     *
     * @param index
     * @param e
     */
    public void add (int index,E e) {
        checkIndexRangeForAdd(index);
        // 确保数组容量未满，能够容纳新添加的元素
        ensureCapacity(size + 1);
        if (index < size) {
            System.arraycopy(this.elementData, index, this.elementData, index + 1, size - index);
        }
        elementData[index] = e;
        size++;
    }

    /**
     * 确保数组的容量至少为指定的容量
     *
     * @param minCapacity
     */
    private void ensureCapacity (int minCapacity) {
        // 将数组空间的开辟延迟到向数组添加元素的时
        int capacity = this.elementData == DEFAULT_INIT_EMPTY_CAPACITY_ELEMENT_DATA ?
                Math.max(DEFAULT_CAPACITY,minCapacity) : minCapacity;

        if (capacity - elementData.length > 0) {
            // 数组空间扩容
            grow (capacity);
        }
    }

    /**
     * <i>8 :</i>
     *
     * 一些vm在数组对象上保留header words。
     * 试图分配更大的数组可能会导致 OutOfMemoryError
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    /**
     * 数组空间扩容算法，确保其空间至少为 minCapacity
     *
     * @param minCapacity
     */
    private void grow (int minCapacity) {

        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);

        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            // 将容量溢出检查向后放-异常状况较正常状况发生概率小得多
            if (minCapacity < 0) {
                throw new OutOfMemoryError("Array current capacity :" + elementData.length
                        + ",the maximum amount of space available is:"+ (Integer.MAX_VALUE - elementData.length));
            }

            newCapacity = MAX_ARRAY_SIZE;
        }

        copyElementData(newCapacity, Math.min(oldCapacity, newCapacity));
    }

    /**
     * 数组空间缩小到合适的容量
     *
     */
    private void downIfNecessary () {
        if ( elementData.length > DEFAULT_CAPACITY
                && elementData.length - size >  (elementData.length >> 1) ) {

            int rightCapacity = Math.max(size,DEFAULT_CAPACITY);
            copyElementData(rightCapacity, size);
        }
    }

    private void copyElementData(int newCapacity, int oldSize) {
        Object[] copy = new Object[newCapacity];
        System.arraycopy(this.elementData, 0, copy, 0, oldSize);
        this.elementData = copy;
    }

    /**
     * 数组索引范围检查
     *
     * @param index
     */
    private void checkIndexRangeForAdd (int index) {
        // 数组动态扩容 因此可以向 index = size 除添加元素
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("size:" + size + " index:" + index);
        }
    }

    private void checkIndexRange (int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("size:" + size + " index:" + index);
        }
    }

    /**
     * 删除第一个元素
     *
     * @return
     */
    public E removeFirst() {
        if (size > 0) {
           return remove(0);
        }
        return null;
    }

    /**
     * 删除最后一个元素
     *
     * @return
     */
    public E removeLast() {
        if (size > 0) {
            return remove(size - 1);
        }
        return null;
    }

    /***
     * 删除指定索引元素，并返回索引处元素。
     *
     * @param index
     * @return
     */
    public E remove(int index) {
        checkIndexRange(index);
        E removedElement = (E) elementData[index];
        fastRemove(index);
        downIfNecessary();
        return removedElement;
    }

    private void fastRemove(int index) {
        int numOfEleMove = size - index - 1;
        if (numOfEleMove > 0 ) {
            System.arraycopy(elementData,index + 1,elementData,index,numOfEleMove);
        }
        elementData[--size] = null;
    }

    /**
     * 删除给定元素，存在多个删除第一个
     * 给定元素不存在返回false
     *
     * @param e
     * @return
     */
    public boolean remove(E e) {

        int index = indexOf(e);
        if (index >= 0) {
            fastRemove(index);
            downIfNecessary();
            return true;
        }

        return false;
    }

    /**
     * 修改指定索引上的元素
     *
     * @param index
     * @param e
     */
    public E set(int index,E e) {
        checkIndexRange(index);

        E oldElement =  (E) elementData[index];
        elementData[index] = e;

        return oldElement;
    }

    /**
     * 取给点索引位置的元素
     *
     * @param index
     * @return
     */
    public E get(int index) {
        checkIndexRange(index);
        return (E) elementData[index];
    }

    /**
     * 给定元素第一个查询到的位置的索引，没有返回-1
     *
     * @param e
     * @return
     */
    public int indexOf(E e) {
        if (e == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                   return i;
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if (elementData[i] .equals(e)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 数组是否包含给点元素
     *
     * @param e
     * @return
     */
    public boolean contains(E e) {
        return indexOf(e) >= 0;
    }


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("{ capacity:"+this.elementData.length + " size:" + size + " element:[");
        for (int i = 0; i < size; i++) {
            ret.append(elementData[i].toString());
            if (i < size - 1) {
                ret.append(",");
            }
        }
        ret.append("] }");
        return ret.toString();
    }
}
