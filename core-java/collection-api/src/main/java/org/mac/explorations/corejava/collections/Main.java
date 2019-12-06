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

package org.mac.explorations.corejava.collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @auther mac
 * @date 2019-11-15
 */
public class Main {

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static void main(String[] args) {
        String s = "Hello World";
        int h = s.hashCode();
        System.out.println("     h:"+ Integer.toBinaryString( h ));
        int t = h >>> 16;
        System.out.println("h>>>16:"+ Integer.toBinaryString( t ));

        System.out.println(Integer.toBinaryString(hash(s)));

        Map<String,Integer> map = new HashMap<>();
        map.put("123",1);
        map.get("123");
        map.containsKey("123");
        map.entrySet();
        map.remove("123");

        Map<String,Integer> lmap = new LinkedHashMap<>();
        lmap.put("123",1);

        Map<Object,Integer> tmap = new TreeMap<>();
        tmap.put("123",1);
        tmap.remove("123");

        PriorityQueue queue = new PriorityQueue();
        queue.add("1");
        queue.remove();
}
}
