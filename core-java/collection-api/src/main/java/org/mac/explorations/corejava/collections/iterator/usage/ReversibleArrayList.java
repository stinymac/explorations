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

package org.mac.explorations.corejava.collections.iterator.usage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 一个简单的可逆向迭代的ArrayList实现
 *
 * @auther mac
 * @date 2019-10-11
 */

public class ReversibleArrayList<T> extends ArrayList<T> {

    public ReversibleArrayList(Collection<T> c) {
        super(c);
    }

    /**
     * 适配器方法 适配for-each
     *
     * @return
     */
    public Iterable<T> reverse() {

        return () -> new Iterator<T>() {

            int current = size() - 1;

            @Override
            public boolean hasNext() {
                return current > -1;
            }

            @Override
            public T next() {
                return get(current--);
            }
        };
    }
}
