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

package org.mac.explorations.corejava.base.generic.usage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @auther mac
 * @date 2019-10-04
 */

public class FilledListMaker<T> {

    List<T> create(T t, int n){
        List<T> result = new ArrayList<>();
        for (int i = 0 ; i < n ; i++) {
            result.add(t);
        }
        return result;
    }

    public static void main(String[] args) {

        FilledListMaker<String> stringFilledListMaker = new FilledListMaker<>();
        List<String> list = stringFilledListMaker.create("Hello",5);

        /**虽然运行时擦除了T的类型信息,但编译时编译器还是可以确保方法或类中的泛型类型信息的内部一致性*/
        //List<Integer> ilist = stringFilledListMaker.create(1,5);

        System.out.println(list);
    }
}
