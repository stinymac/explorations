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

/**
 * 链表
 *
 * @auther mac
 * @date 2019-10-24
 */
public class BasicLinkedList<E> {

    /**
     * 基本链表节点
     *
     * @param <E>
     */
    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }

    /**
     * 链表尺寸
     */
    private int size;

    /**
     * 为方便操作设置一个哑节点，为头节点
     */
    private Node<E> dummyHead;


    public BasicLinkedList() {
        dummyHead = new Node<>(null,null);
    }

    public void addFirst(E e) {
        add(0,e);
    }

    public void addLast(E e) {
        add(size,e);
    }

    /**
     * 链表是不适合随机访问的
     * O(n)
     *
     * @param index
     * @param e
     */
    public void add (int index,E e) {
        checkIndexForAdd(index);

        Node<E> prev = position(index);

        prev.next = new Node<>(e,prev.next);

        size++;
    }

    private Node<E> position(int index) {
        Node<E> it = dummyHead;
        for (int i = 0; i < index; i++) {
            it = it.next;
        }
        return it;
    }

    private void checkIndexForAdd(int index) {
        // index == size 即向最后添加元素
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("size:" + size + " index:" + index);
        }
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        return remove(size - 1);
    }

    /**
     * 删除元素
     *
     * @param e
     */
    public void remove(E e) {

        for (Node<E> it = dummyHead.next,prevNode = dummyHead; it != null; prevNode = it,it = it.next) {

            if ((it.item == null && e == null) || it.item.equals(e)) {
                removeNode(prevNode, it);
                break;
            }
        }
    }

    /**
     * 删除指定位置元素
     *
     * @param index
     * @return
     */
    public E remove(int index) {
        checkIndex(index);

        Node<E> prev = position(index);

        Node<E> del =  prev.next;
        E delElement = del.item;

        removeNode(prev, del);

        return delElement;
    }

    private void checkIndex(int index) {
        // index == size 即向最后添加元素
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("size:" + size + " index:" + index);
        }
    }

    private void removeNode(Node<E> prev, Node<E> del) {
        prev.next = del.next;
        del.next = null;
        del.item = null;
        size--;
    }

    /**
     * 修改指定索引上的元素
     *
     * @param index
     * @param e
     */
    public void set(int index,E e) {
        checkIndex(index);
        // 定位到指定索引的上一个位置
        Node<E> prev = position(index);
        prev.next.item = e;
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
        Node<E> prev = position(index);

        return  prev.next.item;
    }

    /**
     * 给定元素是否存在于链表中
     *
     * @param e
     * @return
     */
    public boolean contains(E e) {

        for (Node<E> it = dummyHead.next; it != null;it = it.next) {

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
        return dummyHead.next == null;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("BasicLinkedList { size:%3d nodes: [ ",size));
        for (Node<E> it = dummyHead.next; it != null; it = it.next) {
            res.append(it.item + "->");
        }
        res.append("NULL ] }");
        return res.toString();
    }
}
