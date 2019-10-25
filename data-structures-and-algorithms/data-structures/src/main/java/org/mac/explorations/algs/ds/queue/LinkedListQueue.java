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
 * 使用链表实现队列
 *
 * @auther mac
 * @date 2019-10-25
 */
public class LinkedListQueue<E> implements Queue<E>  {

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node[" +
                    "item=" + item +
                    ", next=" + ((next == null) ? "NULL" : next.hashCode()) +
                    ']';
        }
    }

    /***
     * 维护一个tail变量指向队尾
     * 使得出队的时间复杂度为O(1)
     */
    private Node<E> front,tail;

    private int size;

    @Override
    public void enqueue(E e) {

        Node<E> newNode = new Node<>(e,null);

        if (tail == null) {
            front = tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new RuntimeException(" Queue is empty");
        }

        E element = front.item;
        if (size == 1) {
            front = tail = null;
        }
        else {
           front = front.next;
        }
        size--;
        return element;
    }

    @Override
    public E headElement() {
        if (isEmpty()) {
            throw new RuntimeException(" Queue is empty");
        }
        return front.item;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("LinkedListQueue { ");
        int numWidth = String.valueOf(size).length();
        strBuilder.append(String.format(" size:%"+numWidth+"d",size()));
        strBuilder.append(" <-[");

        for (Node<E> it = front; it != null; it = it.next) {
            strBuilder.append(it.item);
            if (it.next != null) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("]<- }");

        return strBuilder.toString();
    }
}
