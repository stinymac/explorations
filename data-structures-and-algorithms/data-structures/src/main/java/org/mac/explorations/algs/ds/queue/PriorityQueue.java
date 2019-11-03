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

import org.mac.explorations.algs.ds.heap.MaximumHeap;

/**
 * @auther mac
 * @date 2019-11-03
 */
public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private final MaximumHeap<E> heap;

    public PriorityQueue() {
        this.heap = new MaximumHeap();
    }

    @Override
    public void enqueue(E e) {
        heap.add(e);
    }

    @Override
    public E dequeue() {
        return heap.take();
    }

    @Override
    public E headElement() {
        return heap.max();
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }
}
