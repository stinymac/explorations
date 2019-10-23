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

import org.mac.explorations.algs.ds.array.DynamicArray;

/**
 * 数组实现的队列
 *
 * @auther mac
 * @date 2019-10-23
 */
public class ArrayQueue<E> implements Queue<E> {

    private final DynamicArray<E> queue;

    public ArrayQueue() {
        this.queue = new DynamicArray<>();
    }

    public ArrayQueue(int initCapacity) {
        this.queue = new DynamicArray<>(initCapacity);
    }

    /**
     * 元素从队尾入队
     *
     * @param e
     */
    @Override
    public void enqueue(E e) {
        queue.add(e);
    }

    /**
     * 队首出队
     *
     * 时间复杂度O(n)
     *
     * @return
     */
    @Override
    public E dequeue() {
        return queue.removeFirst();
    }

    @Override
    public E headElement() {
        return queue.get(0);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public String toString() {

        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("Queue { ");
        strBuilder.append(" size:" + size());
        strBuilder.append(" <-[");

        int size = size();
        for (int i = 0; i < size; i++) {
            strBuilder.append(queue.get(i));
            if (i != size - 1) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("]<- }");

        return strBuilder.toString();
    }
}
