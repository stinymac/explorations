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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @date 2019-10-18
 */
public class SimpleHashMap<K,V> extends AbstractMap<K,V> {

    private static final int SIZE = 997;

    LinkedList<Entry<K,V>>[] buckets = new LinkedList[SIZE];

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

    public V put(K key,V value) {
        V oldValue = null;

        int index = Math.abs(key.hashCode()) % SIZE;

        Entry<K,V> entry = new Entry<>(key,value);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
            buckets[index].add(entry);
            return oldValue;
        }

        boolean found = false;
        ListIterator<Entry<K,V>> it = buckets[index].listIterator();
        while (it.hasNext()) {
            Entry<K,V> e = it.next();
            if (key.equals(e.key)) {
                oldValue = e.value;
                it.set(entry);
                found = true;
                break;
            }
        }
        if (!found) {
            buckets[index].add(entry);
        }
        return oldValue;
    }

    public V get(Object key) { // key is type Object, not K
       int index = Math.abs(key.hashCode()) % SIZE;
       LinkedList<Entry<K,V>> bucket = buckets[index];
       if (buckets[index] == null) return null;
       for (Entry<K,V> e : buckets[index]) {
           if (key.equals(e.key)){
               return e.value;
           }
       }
       return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        for (LinkedList<Entry<K,V>> bucket : buckets) {
            if (bucket == null) continue;
            for (Entry<K,V> e : bucket) {
                entries.add(e);
            }
        }
        return entries;
    }
}
