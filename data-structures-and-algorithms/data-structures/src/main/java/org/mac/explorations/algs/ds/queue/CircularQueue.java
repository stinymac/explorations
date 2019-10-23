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

package org.mac.explorations.algs.ds.queue;

/**
 * 循环队列
 *
 * @auther mac
 * @date 2019-10-23
 */
public class CircularQueue<E> implements Queue<E> {

    private Object[] elementData;

    private static final int DEFAULT_CAPACITY = 10;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 队头 队尾索引
     */
    private int front,tail;

    public CircularQueue(int initCapacity) {
        // 为避免队尾在循环的过程中与使队头相撞 空出一个空间
        this.elementData = new Object[initCapacity + 1];
    }

    public CircularQueue() {
        this(DEFAULT_CAPACITY);
    }


    @Override
    public void enqueue(E e) {

        ensureCapacity();

        elementData[tail] = e;
        tail = (tail + 1) % elementData.length;
    }

    /**
     * 如果队列已满，对队列进行扩容
     * 1) front = 0 , tail = capacity
     * 2) tail + 1 = front
     *
     */
    private void ensureCapacity() {

        int oldCapacity = elementData.length;

        if ((tail + 1) % oldCapacity == front) { // (front == 0 && tail == oldCapacity - 1) || tail + 1 == front

            if (MAX_ARRAY_SIZE - oldCapacity <= 0) {
                throw new OutOfMemoryError("Out Of memory");
            }
            int newCapacity = oldCapacity+ (oldCapacity >> 1) + 1;
            newCapacity = (newCapacity < 0 || newCapacity > MAX_ARRAY_SIZE) ? MAX_ARRAY_SIZE : newCapacity;

            int size = oldCapacity - 1;
            copyToNewElementData(size, newCapacity);
        }
    }

    @Override
    public E dequeue() {

        if (isEmpty()) {
            throw  new RuntimeException("Queue is empty");
        }

        E e = (E) elementData[front];
        elementData[front] = null;
        front = (front + 1) % elementData.length;
        
        downIfNecessary();

        return e;
    }

    /**
     *缩减容量
     *
     */
    private void downIfNecessary() {

        int realCapacity = capacity ();
        int size = size();

        if (realCapacity > DEFAULT_CAPACITY && realCapacity - size > (realCapacity >> 1)) {

            int newCapacity = Math.max(DEFAULT_CAPACITY,size + (size >> 1)) + 1;
            copyToNewElementData(size, newCapacity);
        }
    }

    private void copyToNewElementData(int size, int newCapacity) {
        Object[] newElementData = new Object[newCapacity];
        for (int i = 0 ; i < size; i++) {
            newElementData[i] = elementData[(front + i) % elementData.length];
        }
        this.elementData = newElementData;
        this.front = 0;
        this.tail = size;
    }

    @Override
    public E headElement() {
        return (E) elementData[front];
    }

    /**
     * 1)
     *
     * f       t
     * _ _ _ _ _ _ _ _ _ _ _
     *
     * size = tail - front
     *
     * 2)
     *
     *   t   f
     * _ _   _ _ _ _ _ _ _ _
     *
     * size = length - front + tail
     *
     * @return
     */
    @Override
    public int size() {

        int size = front <= tail ? (tail - front) : elementData.length - front + tail;
        return size;
    }

    public int capacity () {
        return elementData.length - 1;
    }

    @Override
    public boolean isEmpty() {
        return front == tail;
    }

    @Override
    public String toString() {

        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("CircularQueue { ");
        int numWidth = String.valueOf(elementData.length).length();
        strBuilder.append(String.format(" capacity:%"+numWidth+"d",capacity ()));
        strBuilder.append(String.format(" size:%"+numWidth+"d",size()));
        strBuilder.append(String.format(" front:%"+numWidth+"d",front));
        strBuilder.append(String.format(" tail:%"+numWidth+"d",tail));
        strBuilder.append(" <-[");

        for (int i = 0; i < elementData.length; i++) {
            strBuilder.append(elementData[i] == null ? "_" : elementData[i]);
            if (i != elementData.length -1) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("]<- }");

        return strBuilder.toString();
    }
}
