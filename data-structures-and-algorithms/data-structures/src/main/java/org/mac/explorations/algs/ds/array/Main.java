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

package org.mac.explorations.algs.ds.array;

/**
 *
 * @auther mac
 * @date 2019-10-21
 */
public class Main {
    public static void main(String[] args) {

        DynamicArray<Integer> ida = new DynamicArray<>();
        ida.add(1);
        ida.add(2);
        ida.add(3);
        ida.add(4);
        ida.addFirst(1);
        ida.addFirst(2);
        ida.addFirst(3);
        ida.addFirst(4);
        ida.addFirst(5);
        ida.addFirst(6);
        ida.addFirst(7);
        System.out.println(ida);

        System.out.println(ida.indexOf(3));
        System.out.println(ida.contains(11));

        System.out.println(ida.get(0));

        System.out.println(ida.set(0,9));
        System.out.println(ida);

        System.out.println(ida.removeFirst());
        System.out.println(ida);

        System.out.println(ida.removeLast());
        System.out.println(ida);

        System.out.println(ida.remove(3));
        System.out.println(ida);

        ida.removeLast();
        ida.removeLast();
        ida.removeLast();
        ida.removeLast();
        System.out.println(ida);
        ida.removeLast();
        ida.removeLast();
        ida.removeLast();
        ida.removeLast();
        System.out.println(ida);

        ida.removeLast();
        ida.removeFirst();
        System.out.println(ida);
    }
}
