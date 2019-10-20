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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @auther mac
 * @date 2019-10-19
 */
public class MapPerformance {

    static List<Measuring<Map<Integer,Integer>>> MAP_MEASUREMENTS = new ArrayList<>();

    static {
        MAP_MEASUREMENTS.add(new Measuring<Map<Integer, Integer>>("put") {
            @Override
            public int execute(Map<Integer, Integer> map, Parameter parameter) {
                int loops = parameter.loops;
                int size = parameter.size;
                for (int i = 0; i < loops; i++) {
                    map.clear();
                    for (int j = 0; j < size; j++)
                        map.put(j, j);
                }
                return loops * size;
            }
        });
        MAP_MEASUREMENTS.add(new Measuring<Map<Integer, Integer>>("get") {
            @Override
            public int execute(Map<Integer, Integer> map, Parameter parameter) {
                int loops = parameter.loops;
                int span = parameter.size * 2;
                for (int i = 0; i < loops; i++)
                    for (int j = 0; j < span; j++)
                        map.get(j);
                return loops * span;
            }
        });
        MAP_MEASUREMENTS.add(new Measuring<Map<Integer, Integer>>("iterate") {
            @Override
            public int execute(Map<Integer, Integer> map, Parameter parameter) {
                int loops = parameter.loops * 10;
                for (int i = 0; i < loops; i++) {
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext())
                        it.next();
                }
                return loops * map.size();
            }
        });
    }
}
