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
import java.util.Set;

/**
 * @auther mac
 * @date 2019-10-19
 */
public class SetPerformance {

    static final List<Measuring<Set<Integer>>> SET_MEASUREMENTS = new ArrayList<>();

    static {
        SET_MEASUREMENTS.add(new Measuring<Set<Integer>>("add") {
            @Override
            public int execute(Set<Integer> set, Parameter parameter) {
                int loops = parameter.loops;
                int size = parameter.size;
                for(int i = 0; i < loops; i++) {
                    set.clear();
                    for(int j = 0; j < size; j++)
                        set.add(j);
                }
                return loops * size;
            }
        });
        SET_MEASUREMENTS.add(new Measuring<Set<Integer>>("contains") {
            @Override
            public int execute(Set<Integer> set, Parameter parameter) {
                int loops = parameter.loops;
                int span = parameter.size * 2;
                for(int i = 0; i < loops; i++)
                    for(int j = 0; j < span; j++)
                        set.contains(j);
                return loops * span;
            }
        });
        SET_MEASUREMENTS.add(new Measuring<Set<Integer>>("iterate") {
            @Override
            public int execute(Set<Integer> set, Parameter parameter) {
                int loops = parameter.loops * 10;
                for(int i = 0; i < loops; i++) {
                    Iterator<Integer> it = set.iterator();
                    while(it.hasNext())
                        it.next();
                }
                return loops * set.size();
            }
        });
    }
}
