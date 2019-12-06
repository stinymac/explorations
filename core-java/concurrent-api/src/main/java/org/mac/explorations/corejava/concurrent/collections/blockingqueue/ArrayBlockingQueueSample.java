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

package org.mac.explorations.corejava.concurrent.collections.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther mac
 * @date 2019-12-03
 */
public class ArrayBlockingQueueSample {

    static class Ball {
        /**
         * 编号
         */
        private String number ;
        /**
         * 颜色
         */
        private String color ;

        public Ball(String number, String color) {
            this.number = number;
            this.color = color;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    private BlockingQueue<Ball> blockingQueue = new ArrayBlockingQueue<>(1);

    public int queueSize() {
        return blockingQueue.size();
    }

    /**
     * 将球放入队列当中,生产者
     * @param ball
     * @throws InterruptedException
     */
    public void produce(Ball ball) throws InterruptedException{
        blockingQueue.put(ball);
    }

    /**
     * 将球从队列当中拿出去，消费者
     * @return
     */
    public Ball consume() throws InterruptedException {
        return blockingQueue.take();
    }

    public static void main(String[] args) {
        final ArrayBlockingQueueSample box = new ArrayBlockingQueueSample();
        ExecutorService executorService = Executors.newCachedThreadPool();

        /**
         * 往箱子里面放入乒乓球
         */
        executorService.submit(() -> {

            int i = 0;
            while (true){
                Thread.sleep(100);
                Ball ball = new Ball("No.:"+i,"RED");
                try {
                    System.out.println(System.currentTimeMillis()+ ":准备往箱子里放入乒乓球:"+ball.getNumber());
                    box.produce(ball);
                    System.out.println(System.currentTimeMillis()+ ":往箱子里放入乒乓球:"+ball.getNumber());
                    System.out.println("put操作后，当前箱子中共有乒乓球:" + box.queueSize() + "个");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }

        });

        /**
         * consumer，负责从箱子里面拿球出来
         */
        executorService.submit(() -> {

            while (true){
                Thread.sleep(100);
                try {
                    System.out.println(System.currentTimeMillis()+ "准备到箱子中拿乒乓球:");
                    Ball ball = box.consume();
                    System.out.println(System.currentTimeMillis()+ "拿到箱子中的乒乓球:"+ball.getNumber());
                    System.out.println("take操作后，当前箱子中共有乒乓球:" + box.queueSize() + "个");
                    System.out.println("========================================================");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
