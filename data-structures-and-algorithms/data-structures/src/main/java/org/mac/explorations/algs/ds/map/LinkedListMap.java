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

import java.util.NoSuchElementException;

/**
 * 链表实现的Map
 *
 * @auther mac
 * @date 2019-11-01
 */
public class LinkedListMap<K,V> implements Map<K,V> {

    private static class Entry<K,V> implements Map.Entry<K,V> {

        private K key;
        private V value;
        Entry<K,V> next;

        public Entry(K key, V value,Entry<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
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
        public V setValue(V value) {
            V ret = this.value;
            this.value = value;
            return ret;
        }

        public boolean equals (Object obj) {
            if (obj instanceof Entry) {
                Entry<K,V> other = (Entry) obj;
                return key.equals(other.key) && value.equals(other.key);
            }
            return false;
        }
    }




    private int size;

    private Entry<K,V> dummyHead;

    public LinkedListMap() {
        this.dummyHead = new Entry<>(null,null,null);
    }

    @Override
    public V put(K key, V value) {
        Entry<K,V> p = position(key);
        if (p == null) {
            Entry<K,V> entry = new Entry<>(key,value,dummyHead.next);
            dummyHead.next = entry;
            size++;
            return value;
        }
        V ret = p.setValue(value);
        return ret;
    }

    /**
     * 定位链表中的key
     *
     * @param key
     * @return
     */
    private  Entry<K,V> position (K key) {

        for (Entry<K,V> p = dummyHead.next; p != null; p = p.next) {
            if ((key == null && p.key == null )
                    || p.key.equals(key)){

                return p;
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (isEmpty()) {
            return null;
        }
        /**
         * 定位删除节点的前一个节点
         */
        Entry<K,V> prev = dummyHead;
        while (prev.next != null) {
            if ((key == null &&  prev.next.key == null )
                    || prev.next.key.equals(key)) {

                break;
            }
            prev = prev.next;
        }

        Entry<K,V> removedNode = prev.next;
        prev.next = removedNode.next;
        removedNode.next = null;
        size--;

        return removedNode.value;
    }

    @Override
    public V get(K key) {

        Entry<K,V> p = position (key);

        if (p != null) {
            return p.value;
        }

        throw new NoSuchElementException("Not exist key:"+key);
    }

    @Override
    public boolean containsKey(K key) {

        Entry<K,V> p = position (key);
        return p != null ? true : false;
    }

    @Override
    public boolean containsValue(V value) {
        Entry<K,V> p = dummyHead.next;

        while (p != null) {
            if ((value == null && p.value == null )
                    || p.value.equals(value)){
                return true;
            }
            p = p.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return dummyHead.next == null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Entry<K,V> p = dummyHead.next; p != null; p = p.next) {
           builder.append("["+p.key+":"+p.value+"]->");
        }
        builder.append("NULL");
        return builder.toString();
    }
}
