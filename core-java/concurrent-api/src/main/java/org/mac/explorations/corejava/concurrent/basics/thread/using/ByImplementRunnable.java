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
 * 线程的创建 -- 通过实现Runnable接口
 * 并将其提交给一个线程 (新建的线程对象或已有线程(线程池))
 *
 * @auther mac
 * @date 2019-11-23
 */
public class ByImplementRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("通过实现Runnable接口，并将其提交给一个线程使用线程");
    }

    public static void main(String[] args) {
        new Thread(new ByImplementRunnable()).start();
    }
}
