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

package org.mac.explorations.corejava.collections.filling;

import org.mac.explorations.corejava.base.Generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * 一个简单的可自动填充的集合
 *
 * @auther mac
 * @date 2019-10-12
 */
public class SimpleAutofillableCollection<T> extends ArrayList<T> {
    /***
     * 适配Generator到Collection的构造器上
     *
     * @param gen
     * @param quantity
     */
    public SimpleAutofillableCollection(Generator<T> gen, int quantity) {

        Objects.requireNonNull(gen);

        for (int i = 0; i < quantity; i++) {
            add(gen.next());
        }
    }

    public static <T> Collection<T> getCollectionFilled(Generator<T> gen, int quantity) {
        return new SimpleAutofillableCollection(gen,quantity);
    }
}
