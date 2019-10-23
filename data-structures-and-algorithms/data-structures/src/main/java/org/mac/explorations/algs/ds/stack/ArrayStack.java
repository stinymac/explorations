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

import org.mac.explorations.algs.ds.array.DynamicArray;

import java.util.EmptyStackException;

/**
 * @auther mac
 * @date 2019-10-22
 */
public class ArrayStack<E> implements Stack<E> {

    private DynamicArray<E> stack;

    public ArrayStack() {
        this.stack = new DynamicArray<>();
    }

    public ArrayStack(int initCapacity) {
        this.stack = new DynamicArray<>(initCapacity);
    }

    @Override
    public E push(E item) {
        stack.add(item);
        return item;
    }

    @Override
    public E pop() {
        return stack.removeLast();
    }

    @Override
    public E peek() {
        int size = stack.size();
        if (size == 0) {
            throw new EmptyStackException();
        }
        return stack.get(size - 1);
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
        strBuilder.append("ArrayStack { ");
        strBuilder.append(" size:" + size());
        strBuilder.append(" capacity:" + stack.capacity());
        strBuilder.append(" element:[");
        int size = size();
        for (int i = size - 1; i >= 0; i--) {
            strBuilder.append(stack.get(i));
            if (i != 0) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("] }");
        return strBuilder.toString();
    }
}
