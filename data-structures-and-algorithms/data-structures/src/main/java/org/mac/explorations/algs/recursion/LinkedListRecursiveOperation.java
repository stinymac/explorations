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

package org.mac.explorations.algs.recursion;

/**
 * 链表递归操作
 *
 * @link  {https://leetcode-cn.com/problems/remove-linked-list-elements/}
 *
 * @auther mac
 * @date 2019-10-25
 */
public class LinkedListRecursiveOperation {

    private static class Node<E> {

        private E e;
        private Node<E> next;

        public Node(E e, Node<E> next) {
            this.e = e;
            this.next = next;
        }
    }

    /**
     * 删除链表中全部的给定元素
     *
     * 递归分解:
     *
     * linkedList = headNode + remainingLinkedList
     *
     * ...
     *
     * lastNode + null
     *
     * 因此对于链表要删除给定元素可以分解为
     *
     * elementsRemovedLinkedList = head + removeElements(remainingLinkedList,e) // 头元素不需要删除
     * 或者
     * elementsRemovedLinkedList = removeElements(remainingLinkedList,e) // 头元素需要删除
     *
     * ...
     *
     * removeElements(null,e) -> return null;
     *
     * @param head
     * @param e
     * @param <E>
     * @return
     */
    public static <E> Node<E> removeElements (Node<E> head,E e) {
        if (head == null) {
            return null;
        }
        head.next = removeElements(head.next,e);
        return (head.e.equals(e)) ? head.next : head;
    }

    public static void main(String[] args) {
        Node<Integer> tail , head = new Node<>(3,null);
        tail =  head;
        for (int i = 0; i < 5; i++) {
           tail.next = new Node<>(i,null);
           tail = tail.next;
        }

        print(head);
        print(removeElements(head,3));
    }

    private static void print(Node<Integer> head) {
        for (Node<Integer> it = head; it != null; it = it.next) {
           System.out.print(it.e + "->");
        }
        System.out.println("NULL");
    }
}
