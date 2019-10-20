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

import org.mac.explorations.corejava.collections.filling.CountingFlyweightList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * List 的测试
 *
 * @auther mac
 * @date 2019-10-18
 */
public class ListPerformance {

    private static final Random RAND = new Random();
    private static final int REPS = 1000;

    static final List<Measuring<List<Integer>>> LIST_MEASUREMENTS = new ArrayList<>();
    static final List<Measuring<LinkedList<Integer>>> QUEUE_MEASUREMENTS = new ArrayList<>();

    static {
        LIST_MEASUREMENTS.add(new Measuring<List<Integer>>("add") {
            @Override
            public int execute(List<Integer> list, Parameter parameter) {
                int loops = parameter.loops;
                int size = parameter.size;

                for (int i = 0; i < loops; i++) {
                    list.clear();
                    for (int j = 0; j < size; j++) {
                        list.add(j);
                    }
                }
                return loops * size;
            }
        });

        LIST_MEASUREMENTS.add(new Measuring<List<Integer>>("get") {
            @Override
            public int execute(List<Integer> list, Parameter parameter) {
                int loops = parameter.loops * REPS;
                int size = list.size();

                for (int i = 0; i < loops; i++) {
                    list.get(RAND.nextInt(size));
                }
                return loops;
            }
        });

        LIST_MEASUREMENTS.add(new Measuring<List<Integer>>("set") {
            @Override
            public int execute(List<Integer> list, Parameter parameter) {
                int loops = parameter.loops * REPS;
                int size = list.size();

                for (int i = 0; i < loops; i++) {
                    list.set(RAND.nextInt(size),47);
                }
                return loops;
            }
        });

        LIST_MEASUREMENTS.add(new Measuring<List<Integer>>("iteradd") {
            @Override
            public int execute(List<Integer> list, Parameter parameter) {
                final int loops = 1000000;
                int half = list.size()/2;
                ListIterator it = list.listIterator(half);
                for (int i = 0; i < loops; i++) {
                   it.add(47);
                }
                return loops;
            }
        });

        LIST_MEASUREMENTS.add(new Measuring<List<Integer>>("insert") {
            @Override
            public int execute(List<Integer> list, Parameter parameter) {
                final int loops = parameter.loops;

                for (int i = 0; i < loops; i++) {
                   list.add(5,47);
                }
                return loops;
            }
        });

        LIST_MEASUREMENTS.add(new Measuring<List<Integer>>("remove") {
            @Override
            public int execute(List<Integer> list, Parameter parameter) {
                int loops = parameter.loops ;
                int size = list.size();

                for (int i = 0; i < loops; i++) {
                    list.clear();
                    list.addAll(new CountingFlyweightList(size));
                    while (list.size() > 5) {
                        list.remove(5);
                    }
                }
                return loops * size;
            }
        });


        QUEUE_MEASUREMENTS.add(new Measuring<LinkedList<Integer>>("addFirst") {
            @Override
            public int execute(LinkedList<Integer> queue, Parameter parameter) {
                int loops = parameter.loops;
                int size = parameter.size;
                for(int i = 0; i < loops; i++) {
                    queue.clear();
                    for(int j = 0; j < size; j++)
                        queue.addFirst(47);
                }
                return loops * size;
            }

        });
        QUEUE_MEASUREMENTS.add(new Measuring<LinkedList<Integer>>("addLast") {
            @Override
            public int execute(LinkedList<Integer> queue, Parameter parameter) {
                int loops = parameter.loops;
                int size = parameter.size;
                for(int i = 0; i < loops; i++) {
                    queue.clear();
                    for(int j = 0; j < size; j++)
                        queue.addLast(47);
                }
                return loops * size;
            }
        });

        QUEUE_MEASUREMENTS.add( new Measuring<LinkedList<Integer>>("rmFirst") {
            @Override
            public int execute(LinkedList<Integer> queue, Parameter parameter) {
                int loops = parameter.loops;
                int size = parameter.size;
                for(int i = 0; i < loops; i++) {
                    queue.clear();
                    queue.addAll(new CountingFlyweightList(size));
                    while(queue.size() > 0)
                        queue.removeFirst();
                }
                return loops * size;
            }
        });

        QUEUE_MEASUREMENTS.add(new Measuring<LinkedList<Integer>>("rmLast") {
            @Override
            public int execute(LinkedList<Integer> queue, Parameter parameter) {
                int loops = parameter.loops;
                int size = parameter.size;
                for(int i = 0; i < loops; i++) {
                    queue.clear();
                    queue.addAll(new CountingFlyweightList(size));
                    while(queue.size() > 0)
                        queue.removeLast();
                }
                return loops * size;
            }
        });
    }

    static class ListTester extends Tester<List<Integer>> {

        public ListTester(List<Integer> container, List<Measuring<List<Integer>>> totalMeasurement) {
            super(container, totalMeasurement);
        }

        @Override
        protected List<Integer> initialize(int size){
            container.clear();
            container.addAll(new CountingFlyweightList(size));
            return container;
        }
        // Convenience method:
        public static void run(List<Integer> list, List<Measuring<List<Integer>>> tests) {
            new ListTester(list, tests).timedMeasure();
        }
    }
}
