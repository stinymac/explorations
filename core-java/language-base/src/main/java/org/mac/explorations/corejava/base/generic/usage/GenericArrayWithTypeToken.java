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

import java.lang.reflect.Array;

/**
 * 类型标记的泛型数组
 *
 * @auther mac
 * @date 2019-10-04
 */

public class GenericArrayWithTypeToken<T> {

    private T[] array;

    public GenericArrayWithTypeToken(Class<T> type,int size) {
        /**
         * 此处的数组类型，在运行时是指定的泛型参数的数组类型
         * 比如: T 指定为 Integer，运行时array的类型为Integer[]
         */
        this.array = (T[]) Array.newInstance(type,size);
    }

    public void put (int index,T item) {
        array[index] = item;
    }

    public T get (int index) {
        return array[index];
    }

    public T[] rep () {
        return array;
    }

    public static void main(String[] args) {

        GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken<>(Integer.class,10);
        Integer[] ia = gai.rep();
    }
}
