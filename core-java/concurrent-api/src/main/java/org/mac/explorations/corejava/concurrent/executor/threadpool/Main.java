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

package org.mac.explorations.corejava.concurrent.executor.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther mac
 * @date 2019-12-05
 */
public class Main {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    private static final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    public static void main(String[] args) throws InterruptedException {

        /*System.out.println( (Integer.SIZE - 3));
        System.out.println( 1<<(Integer.SIZE - 3));

        System.out.println("CAPACITY:"+CAPACITY);

        //00000000000000000000000000000000
        System.out.println("SHUTDOWN:"+SHUTDOWN+"->"+Integer.toBinaryString(SHUTDOWN));
        //00100000000000000000000000000000
        System.out.println("STOP:"+STOP+"->"+Integer.toBinaryString(STOP));
        //01000000000000000000000000000000
        System.out.println("TIDYING:"+TIDYING+"->"+Integer.toBinaryString(TIDYING));
        //01100000000000000000000000000000
        System.out.println("TERMINATED:"+TERMINATED+"->"+Integer.toBinaryString(TERMINATED));
        //11100000000000000000000000000000
        System.out.println("RUNNING:"+RUNNING+"->"+Integer.toBinaryString(RUNNING));

        System.out.println((0b11100000000000000000000000000000
                |0b00000000000000000000000000000000)+ "->"+Integer.toBinaryString(0b11100000000000000000000000000000
                          |0b00000000000000000000000000000000));

        System.out.println("runStateOf(int ctl):"+runStateOf(ctl.get()));
        System.out.println("runStateOf(TIDYING):"+runStateOf(TIDYING));
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        threadPool.submit(() -> System.out.println("running..."));*/

        //SimpleThreadPool pool = new SimpleThreadPool(10,50);
        ThreadPoolExecutor  pool = new  ThreadPoolExecutor(10,50,1, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(10));
        for(int i = 0; i < 100; i++)
            pool.execute(() -> System.out.println("running..."));
        System.out.println(pool.getLargestPoolSize());
        pool.shutdown();
       /* while (true){
            Thread.sleep(1000);
            System.out.println(pool.getLargestPoolSize());

        }*/

    }
}
