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

package org.mac.explorations.algs.ds.set;

/**
 * @auther mac
 * @date 2019-11-01
 */
public interface Set<E> {

    /**
     * 向集合中添加元素，不能有重复元素
     *
     * @param e
     */
    void add(E e);

    /**
     * 集合中删除元素
     *
     * @param e
     */
    void remove(E e);


    /**
     * 集合中元素查找是否存在
     *
     * @param e
     */
    boolean contains(E e);

    int size();

    boolean isEmpty();
}
