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

package org.mac.explorations.algs.ds.stack;

import org.mac.explorations.algs.ds.linkedlist.BasicLinkedList;

import java.util.EmptyStackException;

/**
 * @auther mac
 * @date 2019-10-25
 */
public class LinkedListStack<E> implements Stack<E> {

    private BasicLinkedList <E> stack = new BasicLinkedList<>();

    public LinkedListStack() {
    }

    @Override
    public E push(E item) {
        stack.addFirst(item);
        return item;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.removeFirst();
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.get(0);
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("LinkedListStack { ");
        strBuilder.append(" size:" + size());
        strBuilder.append(" element: <-[");

        strBuilder.append(stack.toString());

        strBuilder.append("] }");
        return strBuilder.toString();
    }
}
