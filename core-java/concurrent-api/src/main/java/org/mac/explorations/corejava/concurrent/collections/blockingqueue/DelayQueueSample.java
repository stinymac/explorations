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

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @auther mac
 * @date 2019-12-03
 */
public class DelayQueueSample {

    static class MovieTicket implements Delayed {
        //延迟时间
        private final long delay;
        //到期时间
        private final long expire;
        //数据
        private final String msg;
        //创建时间
        private final long now;

        public long getDelay() {
            return delay;
        }

        public long getExpire() {
            return expire;
        }

        public String getMsg() {
            return msg;
        }

        public long getNow() {
            return now;
        }

        /**
         * @param msg 消息
         * @param delay 延期时间
         */
        public MovieTicket(String msg , long delay) {
            this.delay = delay;
            this.msg = msg;
            expire = System.currentTimeMillis() + delay;    //到期时间 = 当前时间+延迟时间
            now = System.currentTimeMillis();
        }

        /**
         * @param msg
         */
        public MovieTicket(String msg){
            this(msg,1000);
        }

        public MovieTicket(){
            this(null,1000);
        }

        /**
         * 获得延迟时间   用过期时间-当前时间,时间单位毫秒
         * @param unit
         * @return
         */
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expire
                    - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
        }

        /**
         * 用于延迟队列内部比较排序  当前时间的延迟时间 - 比较对象的延迟时间
         * 越早过期的时间在队列中越靠前
         * @param delayed
         * @return
         */
        public int compareTo(Delayed delayed) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS)
                    - delayed.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "MovieTicket{" +
                    "delay=" + delay +
                    ", expire=" + expire +
                    ", msg='" + msg + '\'' +
                    ", now=" + now +
                    '}';
        }
    }

    public static void main(String[] args) {
        DelayQueue<MovieTicket> delayQueue = new DelayQueue<>();
        MovieTicket ticket1 = new MovieTicket("movie-ticket-1",10000);
        delayQueue.put(ticket1);
        MovieTicket ticket2 = new MovieTicket("movie-ticket-2",5000);
        delayQueue.put(ticket2);
        MovieTicket ticket3 = new MovieTicket("movie-ticket-3",8000);
        delayQueue.put(ticket3);
        System.out.println("message:--->入队完毕");

        MovieTicket ticket;
        while( delayQueue.size() > 0 ){
            try {
                ticket = delayQueue.take();
                System.out.println("电影票出队:"+ticket.getMsg());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
