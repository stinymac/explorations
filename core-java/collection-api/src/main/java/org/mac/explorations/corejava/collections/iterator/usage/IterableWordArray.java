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

import java.util.Iterator;

/**
 * 迭代
 *
 * @auther mac
 * @date 2019-10-11
 */

public class IterableWordArray implements Iterable<String>{

    protected final String [] words;

    public IterableWordArray(String[] words) {
        this.words = words;
    }

    @Override
    public Iterator<String> iterator() {

        return new Iterator<String>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return !(index == words.length);
            }

            @Override
            public String next() {
                return words[index++];
            }
        };
    }
}
