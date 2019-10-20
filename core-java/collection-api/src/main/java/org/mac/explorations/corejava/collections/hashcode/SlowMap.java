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

package org.mac.explorations.corejava.collections.hashcode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @date 2019-10-17
 */

public class SlowMap<K,V> extends AbstractMap<K,V>{

    private final List<K> keys = new ArrayList<>();
    private final List<V> values = new ArrayList<>();

    public V put(K key, V value) {
        V oldValue = get(key); // The old value or null
        if(!keys.contains(key)) {
            keys.add(key);
            values.add(value);
        } else
            values.set(keys.indexOf(key), value);

        return oldValue;
    }

    /**
     * get (K key)
     * get (Object key)
     *
     * both methods have same erasure, yet neither overrides the other
     * 两种方法具有相同的擦除效果，但都不能覆盖另一种方法
     *
     * 因为泛型是Java1.5引入的特性，不是Java一开始就支持的
     *
     * @param key
     * @return
     */
    public V get(Object key) { // key is type Object, not K
        if(!keys.contains(key))
            return null;
        return values.get(keys.indexOf(key));
    }

    private class Entry<K,V> implements Map.Entry<K,V> {

        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            V result = value;
            value = v;
            return result;
        }

        public int hashCode() {
            return (key==null ? 0 : key.hashCode()) ^ (value==null ? 0 : value.hashCode());
        }
        public boolean equals(Object o) {

            if(!(o instanceof Entry)) return false;

            Entry e = (Entry)o;

            return (key == null ? e.getKey() == null : key.equals(e.getKey())) &&
                    (value == null ? e.getValue()== null : value.equals(e.getValue()));
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        // 每次返回KV的副本
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        Iterator<K> ki = keys.iterator();
        Iterator<V> vi = values.iterator();
        while (ki.hasNext()) {
            Entry<K, V> entry = new Entry<>(ki.next(),vi.next());
            //如果不重写hashCode() 默认使用Object.hashCode()每次产生不同的code
            //System.out.println(entry.hashCode());
            entries.add(entry);
        }
        return entries;
    }
}
