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

package org.mac.explorations.corejava.collections.iterator.usage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 多方式迭代
 *
 * @auther mac
 * @date 2019-10-11
 */

public class MultiIterableWordArray extends IterableWordArray {

    public MultiIterableWordArray(String[] words) {
        super(words);
    }

    public Iterable<String> reverse() {

        return () -> new Iterator<String>() {

            int current = words.length - 1;

            @Override
            public boolean hasNext() {
                return current > -1;
            }

            @Override
            public String next() {
                return words[current--];
            }
        };
    }

    public Iterable<String> randomized() {

        return () -> {

            /**
             * @see java.util.ArrayList#ArrayList(java.util.Collection)
             *
             * 浅拷贝数据 不会影响原数据 - Arrays.asList(words)
             *
             */
            List<String> shuffled = new ArrayList<>(Arrays.asList(words));
            Collections.shuffle(shuffled,new Random(47));
            return shuffled.iterator();
        };
    }
}
