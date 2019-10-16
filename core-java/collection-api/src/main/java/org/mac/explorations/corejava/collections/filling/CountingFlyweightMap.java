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

import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 享元
 *
 * @auther mac
 * @date 2019-10-16
 */

public class CountingFlyweightMap extends AbstractMap<Integer,String>{

    private final int size;

    private static final String[] CHARS = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");

    public CountingFlyweightMap(int size) {
        this.size = size < 0 ? 0 : size;
    }

    private class Entry implements Map.Entry<Integer, String> {

        private int  index;

        public Entry(int index) {

            if (index < 0 || index > size -1) {
                throw new IndexOutOfBoundsException("index "+index+" out of bounds:[0,"+(size-1)+"]");
            }

            this.index = index;
        }

        @Override
        public Integer getKey() {
            return index;
        }

        @Override
        public String getValue() {
            return CHARS[index++ % CHARS.length];
        }

        @Override
        public String setValue(String value) {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object o) {
            return Integer.valueOf(index).equals(o);
        }
        public int hashCode() {
            return Integer.valueOf(index).hashCode();
        }
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        Set<Map.Entry<Integer, String>> entries = new LinkedHashSet<>();
        for (int i = 0; i < size; i++) { // 不完全的享元
            entries.add(new Entry(i));
        }
        return entries;
    }
}