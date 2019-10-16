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

package org.mac.explorations.corejava.collections.filling.generator;

import org.mac.explorations.corejava.base.Generator;

import java.util.Iterator;

/**
 * Letters generator
 *
 * @auther mac
 * @date 2019-10-12
 */

public class SimpleLettersGenerator implements Generator<Pair<Integer,String>>,Iterable<Integer>{

    private final int SIZE = 10;
    private int number = 0;
    private char letter = 'A';

    @Override
    public Pair<Integer, String> next() {
        return new Pair<>(number++,""+letter++);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            @Override
            public boolean hasNext() {
                return number < SIZE;
            }

            @Override
            public Integer next() {
                return number++;
            }
        };
    }
}
