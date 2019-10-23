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
 * 队列
 *
 * @auther mac
 * @date 2019-10-23
 */
public interface Queue<E> {
    /**
     * 元素队尾入队
     *
     * @param e
     */
    void enqueue (E e);

    /**
     * 队首元素出队
     *
     * @return
     */
    E dequeue ();

    /**
     * 获取队首元素
     *
     * @return
     */
    E headElement();

    int size();
    boolean isEmpty();
}
