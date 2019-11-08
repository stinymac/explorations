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

package org.mac.explorations.algs.ds.hashtable;

import java.util.Random;

/**
 * 一般哈希函数设计
 *
 * 整型
 *
 * 小范围正整数直接使用
 * 小范围负整数进行偏移
 *
 * 大整数 取模 (模一个素数)
 *
 * 浮点型
 *
 * 转换为整型
 *
 * 字符串
 *
 * "code" = c * 26^3 + o * 26^2 + d * 26^1 + e * 26^0
 *
 * "code" = c * B^3 + o * B^2 + d * B^1 + e * B^0
 *
 * hash(code) =  (c * B^3 + o * B^2 + d * B^1 + e * B^0) % M
 *
 * hash(code) = ((((c * B) + o)*B+d)*B+e)%M
 *
 * hash(code) = ((((c%M * B) + o)  %M *B +d)%M*B+e) %M
 *
 * int hash = 0;
 *
 * for (int i = 0; i < s.length(); i++ {
 *     hash = (hash * B + s.charAt(i)) % M
 * }
 *
 * 复合类型
 *
 * eg. Date: year month day
 *
 * hash(Date) = ((((year%M * B) + month)  %M *B + day)%M
 *
 *
 * Java中的hashCode
 *
 * @auther mac
 * @date 2019-11-08
 */
public class Main {

    public static void main(String[] args) {

        int x = 42;
        int y = -10;
        double z = 3.14;

        // 42
        System.out.println(((Integer) x).hashCode());
        // -10
        System.out.println(((Integer) y).hashCode());
        // 300063655
        System.out.println(((Double) z).hashCode());
        //2301506
        System.out.println("Java".hashCode());

        System.out.println(new Student(3,2,"tom","m").hashCode());

        int nums = 1000000;
        Random random = new Random(nums);
        SimpleHashTable<Integer,Integer> simpleHashTable = new SimpleHashTable<>();
        long startTime = System.nanoTime();
        for (int i = 0; i < nums; i++) {
            int key = random.nextInt(nums);
            if (simpleHashTable.containsKey(key)){
                simpleHashTable.put(key, simpleHashTable.get(key)+1);
            }
            else {
                simpleHashTable.put(key,1);
            }
        }
        System.out.println((System.nanoTime()-startTime)/1000000000.0);
    }
}

class Student {

    private int grade;
    private int cls;

    private String firstName;
    private String lastName;

    public Student(int grade, int cls, String firstName, String lastName) {
        this.grade = grade;
        this.cls = cls;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {

        int b = 31;
        int hash = 0;

        hash = hash * b + grade;
        hash = hash * b + cls;
        hash = hash * b + firstName.hashCode();
        hash = hash * b + lastName.hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }

        Student other = (Student) o;
        return this.grade == other.grade && this.cls == other.cls
                && this.firstName.equals(other.firstName)
                && this.lastName.equals(other.firstName);
    }
}
