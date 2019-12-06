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

package org.mac.explorations.corejava.concurrent.util.atomic;

import sun.misc.Unsafe;

/**
 * @auther mac
 * @date 2019-12-02
 */
public class SimpleAtomicReferenceUpdater {

    static class Person {

        private String name;
        private volatile int age;

        private static final Unsafe unsafe = UnsafeInstance.get();

        private static final long ageOffset;

        static {
            try {
                ageOffset = unsafe.objectFieldOffset(Person.class.getDeclaredField("age"));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("error!");
            }
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public final boolean  compareAndSetAge(int expect,int update) {
            return unsafe.compareAndSwapInt(this,ageOffset,expect,update);
        }
    }

    public static void main(String[] args) {
        Person p = new Person("Jerry",10);
        System.out.println(p.compareAndSetAge(10,20)+":"+p.getAge());
    }
}
