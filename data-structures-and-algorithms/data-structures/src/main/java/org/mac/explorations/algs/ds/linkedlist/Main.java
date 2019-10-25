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

package org.mac.explorations.algs.ds.linkedlist;

/**
 * @auther mac
 * @date 2019-10-25
 */
public class Main {
    public static void main(String[] args) {
        BasicLinkedList<Integer> linkedList = new BasicLinkedList<>();
        for (int i = 0; i < 5; i++) {
            linkedList.addFirst(i);
            System.out.println(linkedList);
            linkedList.addLast(i);
            System.out.println(linkedList);
        }

        linkedList.remove(new Integer(3));
        System.out.println(linkedList);

        linkedList.remove(0);
        System.out.println(linkedList);

        linkedList.removeFirst();
        System.out.println(linkedList);

        linkedList.removeLast();
        System.out.println(linkedList);

        linkedList.set(linkedList.size() - 1,10);
        System.out.println(linkedList);

        System.out.println(linkedList.isEmpty());

        System.out.println(linkedList.get(1));

        System.out.println(linkedList.contains(10));
    }
}
