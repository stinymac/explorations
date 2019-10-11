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

package org.mac.explorations.corejava.collections.iterator;

import org.mac.explorations.corejava.collections.iterator.usage.IterableWordArray;
import org.mac.explorations.corejava.collections.iterator.usage.MultiIterableWordArray;
import org.mac.explorations.corejava.collections.iterator.usage.ReversibleArrayList;

import java.util.Arrays;

/**
 *
 * @auther mac
 * @date 2019-10-11
 */

public class Main {

    public static void main(String[] args) {

        String[] wordArray = "Although I can't my heart is longing for it".split(" ");

        IterableWordArray iwa = new IterableWordArray(wordArray);
        /**
         * 任何实现了Iterable接口的类都可以使用for-each语法
         */
        iterate(iwa);

        ReversibleArrayList<String> ral = new ReversibleArrayList<>(Arrays.asList(wordArray));
        iterate(ral.reverse());

        MultiIterableWordArray miwa = new MultiIterableWordArray(wordArray);
        iterate(miwa.reverse());
        iterate(miwa.randomized());
    }

    public static <T> void iterate(Iterable<T> iterable) {
        for(T w : iterable) {
            System.out.print(w + " ");
        }
        System.out.println();
    }
}
