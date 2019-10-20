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

package org.mac.explorations.corejava.collections.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;

/**
 * @auther mac
 * @date 2019-10-18
 */
public class Main {
    public static void main(String[] args) {

        ListPerformance.ListTester.run(new ArrayList<>(), ListPerformance.LIST_MEASUREMENTS);
        ListPerformance.ListTester.run(new LinkedList<>(), ListPerformance.LIST_MEASUREMENTS);
        ListPerformance.ListTester.run(new Vector<>(), ListPerformance.LIST_MEASUREMENTS);

        Tester.FIELD_WIDTH= 12;
        Tester<LinkedList<Integer>> qTest = new Tester<>(new LinkedList<>(), ListPerformance.QUEUE_MEASUREMENTS);
        qTest.setHeadline("Queue tests");
        qTest.timedMeasure();


        Tester.FIELD_WIDTH = 10;
        Tester.run(new TreeSet<>(), SetPerformance.SET_MEASUREMENTS);
        Tester.run(new HashSet<>(), SetPerformance.SET_MEASUREMENTS);
        Tester.run(new LinkedHashSet<>(), SetPerformance.SET_MEASUREMENTS);

        Tester.run(new TreeMap<>(), MapPerformance.MAP_MEASUREMENTS);
        Tester.run(new HashMap<>(), MapPerformance.MAP_MEASUREMENTS);
        Tester.run(new LinkedHashMap<>(),MapPerformance.MAP_MEASUREMENTS);
        Tester.run(new IdentityHashMap<>(), MapPerformance.MAP_MEASUREMENTS);
        Tester.run(new WeakHashMap<>(), MapPerformance.MAP_MEASUREMENTS);
        Tester.run(new Hashtable<>(), MapPerformance.MAP_MEASUREMENTS);
    }
}
