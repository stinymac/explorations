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

package org.mac.explorations.algs.ds.linkedlist;

import java.util.NoSuchElementException;

/**
 * 双向链表
 *
 * @see {@link java.util.LinkedList}
 *
 * @auther mac
 * @date 2019-10-25
 */
public class DoublyLinkedList<E> {

    /**
     * 双向链表节点
     *
     * @param <E>
     */
    private static class Node<E> {

        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * 头节点
     */
    private Node<E> first;

    /**
     * 尾节点
     */
    private Node<E> last;

    /**
     * 链表尺寸
     */
    private int size;

    public DoublyLinkedList() {
    }

    public void addFirst(E element) {
        linkedFirst(element);
    }

    private void linkedFirst(E element) {
        final Node<E>  f = first;
        final Node<E> newNode = new Node<>(null,element,f);
        first = newNode;
        if (f == null) {
            last = newNode;
        }
        else {
            f.prev = newNode;
        }
        size++;
    }

    /**
     * 向链表尾添加元素
     *
     * @param element
     */
    public void addLast(E element) {
        linkedLast(element);
    }

    private void linkedLast(E element) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l,element,null);

        last = newNode;

        if (l == null) {
            first = newNode;
        }
        else  {
            l.next = newNode;
        }

        size++;
    }

    /**
     * 向链表中给定的索引处插入元素
     *
     * @param index
     * @param element
     */
    public void add(int index, E element) {
        checkIndexForAdd(index);
        if (index == size) {
            linkedLast(element);
        }
        else {
            linkedBefore(node(index),element);
        }
    }

    /**
     * 给定节点之前插入节点
     *
     * @param node
     * @param element
     */
    private void linkedBefore(Node<E> node,E element) {

        final Node<E> p = node;
        final Node<E> newNode = new Node<>(p.prev,element,p);

        if (p.prev == null) {
           first = newNode;
        }
        else {
            p.prev.next = newNode;
        }
        p.prev = newNode;

        size++;
    }

    /**
     * 定位索引元素
     *
     * @param index
     * @return
     */
    private Node<E> node(int index) {

        if (index < (size >> 1)) {
            Node<E> p = first;
            for (int i = 0; i < index; i++)
                p = p.next;
            return p;
        } else {
            Node<E> p = last;
            for (int i = size - 1; i > index; i--)
                p = p.prev;
            return p;
        }
    }

    private void checkIndexForAdd(int index) {
        // index == size 即向最后添加元素
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("size:" + size + " index:" + index);
        }
    }


    /**
     * 删除元素
     *
     * @param element
     */
    public void remove(E element) {

        for (Node<E> i = first; i != null; i = i.next) {
            if ((element == null && i.item == null ) || element.equals(i.item)) {
                unlink(i);
                return;
            }
        }
    }

    /**
     * 链表中移除一个节点
     *
     * @param node
     */
    private E unlink(Node<E> node) {

        final Node<E> p = node;
        final E element = p.item;

        if (p.prev == null && p.next != null) {
            first = p.next;
            p.next.prev = null;
            p.next = null;
        }
        else if (p.next == null && p.prev != null) {
            last = p.prev;
            p.prev.next = null;
            p.prev = null;
        }
        else if (p.prev == null && p.next == null) {
            first = last = null;
        }
        else {
            p.prev.next = p.next;
            p.next.prev = p.prev;
            p.prev = p.next = null;
        }

        p.item = null;
        size--;

        return element;
    }

    /**
     * 删除指定位置元素
     *
     * @param index
     * @return
     */
    public E remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }

    private E unlinkFirst(Node<E> f) {

        final E element = f.item;
        final Node<E> next = f.next;

        f.item = null;
        f.next = null; // help GC
        first = next;
        if (next == null) { // 关注是否只剩下一个元素
            last = null;
        }
        else {
            next.prev = null;
        }

        size--;

        return element;
    }

    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return unlinkLast(l);
    }

    private E unlinkLast(Node<E> l) {

        final E element = l.item;
        final Node<E> prev = l.prev;

        l.item = null;
        l.prev = null; // help GC
        last = prev;
        if (prev == null) { // 关注是否只剩下一个元素
            first = null;
        }
        else {
            prev.next = null;
        }

        size--;

        return element;
    }

    private void checkIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("size:" + size + " index:" + index);
        }
    }


    /**
     * 修改指定索引上的元素
     *
     * @param index
     * @param element
     */
    public void set(int index,E element) {
        checkIndex(index);

        Node<E> p = node(index);
        p.item = element;
    }

    /**
     * 返回指定索引上的元素
     *
     * @param index
     * @return
     */
    public E get (int index) {
        checkIndex(index);
        // 定位到指定索引的上一个位置
        Node<E> p = node(index);

        return  p.item;
    }

    /**
     * 给定元素是否存在于链表中
     *
     * @param e
     * @return
     */
    public boolean contains(E e) {

        for (Node<E> it = first; it != null;it = it.next) {

            if ((it.item == null && e == null) || it.item.equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当前链表大小
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 链表是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("DoublyLinkedList { size:%3d nodes: [ ",size));
        res.append("NULL");
        for (Node<E> it = first; it != null; it = it.next) {
            res.append("<-|"+it.item + "|->");
        }
        res.append("NULL ] }");
        return res.toString();
    }

}
