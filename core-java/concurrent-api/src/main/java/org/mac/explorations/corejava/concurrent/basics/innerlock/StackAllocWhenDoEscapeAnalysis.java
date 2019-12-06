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

package org.mac.explorations.corejava.concurrent.basics.innerlock;

/**
 * 进行两种测试
 *
 * 关闭逃逸分析，同时调大堆空间，避免堆内GC的发生，如果有GC信息将会被打印出来
 * VM运行参数：-Xmx4G -Xms4G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 *
 * 开启逃逸分析(默认是开启的)
 * VM运行参数：-Xmx4G -Xms4G -XX:+DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 *
 * 执行main方法后
 * jps 查看进程
 * jmap -histo 进程ID
 *
 * ====================
 *
 * 关闭逃逸分析 - 当前程序会在堆上创建500000个对象
 * 开启逃逸分析 - 当前程序只会在堆上创建约16万个对象，其余在栈上。
 *
 *
 * @auther mac
 * @date 2019-11-29
 */
public class StackAllocWhenDoEscapeAnalysis {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            alloc();
        }
        long end = System.currentTimeMillis();
        //查看执行时间
        System.out.println("cost-time " + (end - start) + " ms");
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private static Person alloc() {
        //Jit对编译时会对代码进行 逃逸分析
        //并不是所有对象存放在堆区，有的一部分存在线程栈空间
        Person p = new Person();
        return p;
    }

    static class Person {
        private String name;
        private int age;
    }
}
