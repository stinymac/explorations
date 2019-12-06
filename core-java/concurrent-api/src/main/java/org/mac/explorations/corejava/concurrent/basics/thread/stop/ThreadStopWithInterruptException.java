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

package org.mac.explorations.corejava.concurrent.basics.thread.stop;

/**
 *
 *
 * @auther mac
 * @date 2019-11-23
 */
public class ThreadStopWithInterruptException {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {

            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("task run...");
                try {
                    /**
                     * sleep 响应中断抛出异常就会清除中断状态
                     */
                    Thread.sleep(1000L);
                    /**
                     * 响应中断 抛出异常 有客户端确定如何处理:
                     * 继续或抛出异常或恢复中断等
                     */
                } catch (InterruptedException e) {
                    System.err.println("interrupt");
                    //抛出异常
                    //throw new RuntimeException(e.getMessage(), e.getCause());
                    //恢复中断
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();
        Thread.sleep(500L);
        t.interrupt();
    }
}
