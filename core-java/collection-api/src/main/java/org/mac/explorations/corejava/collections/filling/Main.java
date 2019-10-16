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
import org.mac.explorations.corejava.collections.filling.generator.SimpleLettersGenerator;
import org.mac.explorations.corejava.collections.filling.generator.SimpleStringGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 集合填充
 *
 * @auther mac
 * @date 2019-10-12
 */

public class Main {
    public static void main(String[] args) {

        Generator<String> strGen = new SimpleStringGenerator();
        Set<String> stringSet1 = new LinkedHashSet<>(new SimpleAutofillableCollection<>(strGen,15));
        System.out.println(stringSet1);

        Set<String> stringSet2 = new LinkedHashSet<>();
        stringSet2.addAll(SimpleAutofillableCollection.getCollectionFilled(strGen,15));
        System.out.println(stringSet2);

        Iterable<Integer> keys = new SimpleLettersGenerator();
        Generator<Pair<Integer,String>> letterGen = new SimpleLettersGenerator();
        System.out.println(SimpleAutofillableMap.getMapFilled(keys,letterGen));

        System.out.println("==============================================================");

        System.out.println(Countries.capitals(10));
        System.out.println(new HashMap<>(Countries.capitals(3)));
        System.out.println(new LinkedHashMap<>(Countries.capitals(3)));
        System.out.println(new TreeMap<>(Countries.capitals(3)));
        System.out.println(new Hashtable<>(Countries.capitals(3)));
        System.out.println(new HashSet<>(Countries.names(6)));
        System.out.println(new LinkedHashSet<>(Countries.names(6)));
        System.out.println(new TreeSet<>(Countries.names(6)));
        System.out.println(new ArrayList<>(Countries.names(6)));
        System.out.println(new LinkedList<>(Countries.names(6)));
        System.out.println(Countries.capitals().get("CHINA"));
        System.out.println(Countries.names());

        System.out.println("==============================================================");
        System.out.println(new CountingFlyweightList(30));

        System.out.println("==============================================================");
        System.out.println(new CountingFlyweightMap(30));
    }
}
