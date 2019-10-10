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
 * 泛型数组
 *
 * @auther mac
 * @date 2019-10-04
 */

public class GenericArray<T> {

    private T[] array;

    public GenericArray(int size) {
        // array = (T[]) new T[size]; 不能new 一个泛型数组
        /**由于类型擦除 将其转型为T[] 那么编译时数组的实际类型就将丢失*/
        array = (T[]) new Object[size];
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
        GenericArray<Integer> gai = new GenericArray<>(10);
        gai.put(0,0);
        //编译器自动插入类型转换
        Integer val = gai.get(0);
        System.out.println(val.getClass());

        /**java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.Integer;*/
        //Integer[] ia = gai.rep();
        /**因为泛型擦除，数组实际运行时的类型只能是Object[]*/
        Object[] a = gai.rep();
    }
}
