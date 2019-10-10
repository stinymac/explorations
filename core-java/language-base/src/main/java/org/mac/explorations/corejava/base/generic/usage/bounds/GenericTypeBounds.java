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

package org.mac.explorations.corejava.base.generic.usage.bounds;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @auther mac
 * @date 2019-10-04
 */

public class GenericTypeBounds {

    public static void main(String[] args) {
        /**
         * 一个Number的未知子类
         *
         * 因此producer中事实上无法添加任何类型的数据
         **/
        List<? extends Number> producer =new ArrayList<>();
        //producer.add(new Integer(1));
        Number number = new Number() {
            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }
        };
        //producer.add(number);
        //producer.add(new Long(2));

        List<Number> l = new ArrayList<>();
        l.add(1);
        l.add(new Long(2));

        producer = l;

        Integer i = (Integer)producer.get(0);
        System.out.println(i);

        List<Integer> li = new ArrayList<>();
        li.add(1);

        List<Long> ll = new ArrayList<>();
        ll.add(1L);

        List<String> ls = new ArrayList<>();
        ls.add("1");

        producer = li;
        producer = ll;

        //error
        //producer = ls;

        List lis = new ArrayList<>();
        // unchecked
        producer = lis;

        /**
         * Number的任意类型的子类
         * 因此无法将任何确定的List<Number>赋值给consumer
         **/
        List<? super Number> consumer = new ArrayList<>();
        consumer.add(new Integer(1));
        consumer.add(new Long(2));
        consumer.add(number);

        List<Number> ln = new ArrayList<>();
        List<Double> ld = new ArrayList<>();

        //error
        //consumer = li
        //consumer = ll;
        //consumer = ld;
        consumer = ln;

    }
}
