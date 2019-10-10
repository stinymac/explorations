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

/**
 * 内部数组声明为Object[]的泛型数组
 *
 * @auther mac
 * @date 2019-10-04
 */

public class GenericArrayByObjectArray<T> {
    /**
     * 将内部数组声明为Object[],而在访问的时候
     * 转型为T
     *
     */
    private Object[] array;

    public GenericArrayByObjectArray(int size) {
        array =  new Object[size];
    }

    public void put (int index,T item) {
        array[index] = item;
    }

    public T get (int index) {
        return (T) array[index];
    }

    /**
     * 产生一个运行时异常:java.lang.ClassCastException
     */
    public T[] rep () {
        return (T[]) array;
    }

    public static void main(String[] args) {
        GenericArrayByObjectArray<Integer> gai = new GenericArrayByObjectArray<>(10);
        for (int i = 0; i < 10; i++) {
            gai.put(i,i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(gai.get(i));
        }
        gai.rep();

    }
}
