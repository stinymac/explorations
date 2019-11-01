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

import org.mac.explorations.algs.ds.linkedlist.BasicLinkedList;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @auther mac
 * @date 2019-11-01
 */
public class LinkedListMap<K,V> implements Map<K,V> {

    private static class Entry<K,V> implements Map.Entry<K,V> {

        private K key;
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


    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }

    private int size;

    private Node<Map.Entry<K,V>> dummyHead;

    public LinkedListMap() {
        this.dummyHead = new Node<>(null,null);
    }

    @Override
    public V put(K key, V value) {
        Node<Map.Entry<K,V>> p = position(key);
        if (p == null) {
            Map.Entry<K,V> entry = new Entry<>(key,value);
            Node<Map.Entry<K,V>> newNode = new Node<>(entry,dummyHead.next);
            dummyHead.next = newNode;
            size++;
            return value;
        }
        V ret = p.item.setValue(value);
        return ret;
    }

    /**
     * 定位链表中的key
     *
     * @param key
     * @return
     */
    private  Node<Map.Entry<K,V>> position (K key) {

        for (Node<Map.Entry<K,V>> p = dummyHead.next; p != null; p = p.next) {
            if ((key == null && p.item.getKey() == null )
                    || p.item.getKey().equals(key)){

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
        Node<Map.Entry<K,V>> prev = dummyHead;
        while (prev.next != null) {
            if ((key == null &&  prev.next.item.getKey() == null )
                    || prev.next.item.getKey().equals(key)) {

                break;
            }
            prev = prev.next;
        }

        Node<Map.Entry<K,V>> removedNode = prev.next;
        prev.next = removedNode.next;
        removedNode.next = null;
        size--;

        return removedNode.item.getValue();
    }

    @Override
    public V get(K key) {

        Node<Map.Entry<K,V>> p = position (key);

        if (p != null) {
            return p.item.getValue();
        }

        throw new NoSuchElementException("Not exist key:"+key);
    }

    @Override
    public boolean containsKey(K key) {

        Node<Map.Entry<K,V>> p = position (key);
        return p != null ? true : false;
    }

    @Override
    public boolean containsValue(V value) {
        Node<Map.Entry<K,V>> p = dummyHead.next;

        while (p != null) {
            if ((value == null && p.item.getValue() == null )
                    || p.item.getValue().equals(value)){
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
        for (Node<Map.Entry<K,V>> p = dummyHead.next; p != null; p = p.next) {
           builder.append("["+p.item.getKey()+":"+p.item.getValue()+"]->");
        }
        builder.append("NULL");
        return builder.toString();
    }
}
