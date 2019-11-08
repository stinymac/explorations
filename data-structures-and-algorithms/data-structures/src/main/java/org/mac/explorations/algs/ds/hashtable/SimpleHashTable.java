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

package org.mac.explorations.algs.ds.hashtable;

import java.util.TreeMap;

/**
 * 简单 hash table 由于使用了 java.util.TreeMap
 * 要求 K extends Comparable<K>
 *
 * @auther mac
 * @date 2019-11-08
 */
public class SimpleHashTable<K extends Comparable<K>,V> {

    private static final int UPPER_TOL = 10;
    private static final int LOWER_TOL = 2;

    private static final int[] PRIME_NUMBERS = {
            53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593,
            49157, 98317, 196613, 393241, 786433, 1572869, 3145739, 6291469,
            12582917, 25165843, 50331653, 100663319, 201326611, 402653189, 805306457, 1610612741
    };
    private static int INDEX = 0;
    private static final int DEFAULT_INIT_CAPACITY = PRIME_NUMBERS[INDEX];

    private TreeMap<K,V>[] hashTable;
    private int capacity;
    private int size;

    public SimpleHashTable() {
        this.capacity = DEFAULT_INIT_CAPACITY;
        this.hashTable = new TreeMap[capacity];
    }



    public int size() {
        return size;
    }

    /**
     * key.hashCode() & 0x7fffffff
     * 去掉符号
     * 0x7fffffff = 0111 1111 1111 1111 1111 1111 1111 1111
     *
     * @param key
     * @return
     */
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    public V put(K key,V value) {
        int hash = hash(key);
        TreeMap<K,V> p = hashTable[hash];
        V retVal;
        if(p == null) {
            p = hashTable[hash] = new TreeMap<>();
            p.put(key,value);
            size++;
            retVal =  value;
        }
        else {
            if(p.containsKey(key)) {
                V oldValue = p.get(key);
                p.put(key, value);
                retVal = oldValue;
            }
            else {
                p.put(key,value);
                size++;
                retVal = value;
            }
        }

        if (size > capacity * UPPER_TOL
                && INDEX + 1 < PRIME_NUMBERS.length) {

            resize(PRIME_NUMBERS[++INDEX]);
        }

        return retVal;
    }

    private void resize(int newCapacity) {
        TreeMap<K,V>[] newHashTable = new TreeMap[newCapacity];
        int oldCapacity = this.capacity;
        this.capacity = newCapacity;
        for (int i = 0; i < oldCapacity; i++) {
            for (K key : hashTable[i].keySet()) {
                int hash = hash(key);
                TreeMap<K,V> p = newHashTable[hash(key)];
                if (p == null) {
                    p = newHashTable[hash] = new TreeMap<>();
                }
                p.put(key,hashTable[i].get(key));
            }
        }
        this.hashTable = newHashTable;
    }

    public V remove(K key) {

        TreeMap<K,V> p = hashTable[hash(key)];

        if (p != null) {
            V retVal = p.remove(key);
            size--;
            if (size < capacity * LOWER_TOL
                    && (1 << capacity) > DEFAULT_INIT_CAPACITY
                    && INDEX - 1 >= 0 ) {
                resize(PRIME_NUMBERS[--INDEX] );
            }
            return retVal;
        }

        return null;
    }

    public boolean containsKey(K key){

        TreeMap<K,V> p = hashTable[hash(key)];

        if (p != null) {
            return p.containsKey(key);
        }

        return false;
    }

    public V get(K key){

        TreeMap<K,V> p = hashTable[hash(key)];

        if (p != null) {
            return p.get(key);
        }

        return null;
    }
}
