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

package org.mac.explorations.algs.ds.map;

/**
 *
 * @auther mac
 * @date 2019-11-01
 */
public interface Map<K,V> {

    V put(K key,V value);

    V remove (K key);

    V get(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    int size();

    boolean isEmpty();

    interface Entry<K,V> {

        K getKey();

        V getValue();

        V setValue(V value);
    }
}
