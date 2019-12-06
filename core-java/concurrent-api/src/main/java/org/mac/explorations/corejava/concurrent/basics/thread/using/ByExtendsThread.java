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

package org.mac.explorations.corejava.concurrent.basics.thread.using;

/**
 * 继承Thread类 -- 重写Thread类的run方法
 *
 * @auther mac
 * @date 2019-11-23
 */
public class ByExtendsThread extends Thread{

    @Override
    public void run () {
        System.out.println("通过重写Thread类的run方法，使用线程。");
    }

    public static void main(String[] args) {
        new ByExtendsThread().start();
    }
}
