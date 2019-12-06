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

package org.mac.explorations.corejava.concurrent.basics.thread.exception;

/**
 * @auther mac
 * @date 2019-11-24
 */
public class SimpleUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println((t.getName()+" throw a exception:"+e));
    }

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            throw new RuntimeException("This is a runtime exception");
        });
        t.setUncaughtExceptionHandler(new SimpleUncaughtExceptionHandler());
        t.start();
    }
}
