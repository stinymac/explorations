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

package org.mac.explorations.corejava.base.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * 泛型
 *
 * @auther mac
 * @date 2019-10-03
 */

public class BasicGenericUsage {

    public static void main(String[] args) {


        new Container<String>("Hello,World");
        new Container<CharSequence>("Hello,World");
        /**Container(java.lang.StringBuilder) in Container cannot be applied to (java.lang.String)*/
        //new Container<StringBuilder>("Hello,World");

        /** error: cannot infer arguments */
        //Container<StringBuffer>  c1 = new Container<>("Hello,World");
        Container<CharSequence>  c2 = new Container<>("Hello,World");
        /**Incompatible types.*/
        //Container<CharSequence>  c2 = new Container<String>("Hello,World");
        //Container<String>  c2 = new Container<CharSequence>("Hello,World");


        Container<?>  c3 = new Container<>("Hello,World");

        Container<StringBuffer>  c4 = new Container<>(new StringBuffer("Hello,World"));

        Container<StringBuffer>  c5 = new Container("Hello,World");

        Container<StringBuilder>  c6 = new Container("Hello,World");

        Container  c7 = new Container<CharSequence>("Hello,World");

        /**Wildcard type '?' cannot be instantiated directly*/
        //Container<CharSequence>  c = new Container<?>("Hello,World");

        Container<?>  c8 = new Container<CharSequence>("Hello,World");

        Container<CharSequence>  c9 = new Container<CharSequence>("Hello,World");

        System.out.println(Arrays.toString(c9.getClass().getTypeParameters()));//[E]

        /**
         * Java 泛型对象操作时，看申明对象泛型参数类型
         * 运行时调用的是： java.io.PrintStream#println(java.lang.Object)
         */
        System.out.println(c4.getElement()); // Hello,World

        /**
         * java.lang.ClassCastException:
         * java.lang.String cannot be cast to java.lang.StringBuffer
         */
        StringBuffer v = c4.getElement();
        System.out.println(v.getClass());

        // c4 对象申明的类型为 Container<StringBuffer>，
        // E 类型为 StringBuffer，因此 set(E) ，E必须是 StringBuffer
        c4.set(new StringBuffer("2019"));
        System.out.println(c4.getElement()); // 2019


        add(new ArrayList<>(), "Hello,World");
        add(new ArrayList<>(), 256); // auto-boxing 256 = new Integer(256) <- Integer.valueOf(256)
        add(new HashSet<>(), 2);

        forEach(Arrays.asList(7, 8, 9), System.out::println);
    }

    public static class Container<E extends CharSequence> {

        private E element;

        public Container(E e) {
            this.element = e;
        }


        // operations
        public boolean set(E e) {
            this.element = e;
            return true;
        }

        public E getElement() {
            return element;
        }
    }


    // 多界限泛型参数类型
    public static class C {
    }

    public interface I1 {
    }

    public interface I2 {
    }

    // 多界限泛型参数类型 extends 第一个类型允许是具体类（也可以接口）
    // 第二或更多参数类型必须是接口
    public static class Template<T extends Serializable & I1 & I2> {
    }

    public static class TClass /** extends C */ implements Serializable, I1, I2 {
    }

    //泛型⽅法和有界类型参数
    public static <C extends Collection<E>, E extends Serializable> void add(C target, E element) {
        target.add(element);
    }

    public static <C extends Iterable<E>, E extends Serializable> void forEach(C source, Consumer<E> consumer) {
        for (E e : source) {
            consumer.accept(e);
        }
    }

}


