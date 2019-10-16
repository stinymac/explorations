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
import org.mac.explorations.corejava.collections.filling.generator.Pair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 一个简单的可自动填充的map
 *
 * @auther mac
 * @date 2019-10-12
 */
public class SimpleAutofillableMap<K,V> extends LinkedHashMap<K,V>{

    public SimpleAutofillableMap(Generator<Pair<K,V>> gen, int quantity) {

        Objects.requireNonNull(gen);

        for (int i = 0; i < quantity; i++) {
            Pair<K,V> p = gen.next();
            put(p.getKey(),p.getValue());
        }
    }

    public SimpleAutofillableMap(Generator<K> kGen,Generator<V> vGen, int quantity) {

        Objects.requireNonNull(kGen);
        Objects.requireNonNull(vGen);

        for (int i = 0; i < quantity; i++) {
            put(kGen.next(),vGen.next());
        }
    }

    public SimpleAutofillableMap(Generator<K> kGen,V value, int quantity) {

        Objects.requireNonNull(kGen);

        for (int i = 0; i < quantity; i++) {
            put(kGen.next(),value);
        }
    }

    public SimpleAutofillableMap(Iterable<K> keys,Generator<V> vGen) {

        Objects.requireNonNull(vGen);

        for (K k : keys) {
            put(k,vGen.next());
        }
    }

    public SimpleAutofillableMap(Iterable<K> keys,V value) {

        for (K k : keys) {
            put(k,value);
        }
    }

    public static <K,V> Map<K,V> getMapFilled(Generator<Pair<K,V>> gen, int quantity) {
        return new SimpleAutofillableMap(gen,quantity);
    }

    public static <K,V> Map<K,V> getMapFilled(Generator<K> kGen,Generator<V> vGen, int quantity) {
        return new SimpleAutofillableMap(kGen,vGen,quantity);
    }

    public static <K,V> Map<K,V> getMapFilled(Generator<K> kGen,V value, int quantity) {
        return new SimpleAutofillableMap(kGen,value,quantity);
    }

    public static <K,V> Map<K,V> getMapFilled(Iterable<K> keys, Generator<V> vGen) {
        return new SimpleAutofillableMap(keys,vGen);
    }

    public static <K,V> Map<K,V> getMapFilled(Iterable<K> keys,V value) {
        return new SimpleAutofillableMap(keys,value);
    }
}
