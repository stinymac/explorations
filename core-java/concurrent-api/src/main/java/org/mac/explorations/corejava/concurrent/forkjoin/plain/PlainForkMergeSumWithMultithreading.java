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

package org.mac.explorations.corejava.concurrent.forkjoin.plain;

import org.mac.explorations.corejava.concurrent.forkjoin.Utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 对一个大数据量的数组使用分治的思想并发求和
 *
 * @auther mac
 * @date 2019-12-04
 */
public class PlainForkMergeSumWithMultithreading {

    // 拆分粒度 即最小单元的大小
    private static final int GRAIN_SIZE = 1000;

    public static long sum(int[] array, ExecutorService executorService) throws ExecutionException, InterruptedException {

        if (array == null || executorService == null)
            throw new IllegalArgumentException("error args");

        long sum = 0;
        if (array.length == 0)
            return sum;

        int nTasks = array.length/GRAIN_SIZE > 0 ? array.length/GRAIN_SIZE : 0;
        Future<Long>[] taskFuture = new Future[nTasks] ;

        for (int i = 0; i < nTasks; i++) {
            SumTask task = new SumTask(array,i * GRAIN_SIZE,(i+1) * GRAIN_SIZE);
            taskFuture[i] = executorService.submit(task);
        }

        for (int i = 0; i < nTasks; i++) {
            sum += taskFuture[i].get();
        }

        return sum;
    }


    static class SumTask implements Callable<Long> {

        private final int[] array;
        private final int lo;
        private final int hi;

        SumTask(int[] array, int lo, int hi) {
            if (array == null || lo < 0 || hi < lo || hi > array.length)
                throw new IllegalArgumentException("error arg");
            this.array = array;
            this.lo = lo;
            this.hi = hi;
        }


        @Override
        public Long call() throws Exception {
            return Utils.rangSum(this.array,this.lo,this.hi);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] array = Utils.buildRandomIntArray(20000000);
        int nCPU = Runtime.getRuntime().availableProcessors();
        System.out.println(nCPU);
        ExecutorService executorService = Executors.newFixedThreadPool(nCPU<<1);
        long startTimestamp = System.nanoTime();
        long sum = sum(array,executorService);
        long endTimestamp = System.nanoTime();
        System.out.println("sum:"+sum+ " cost:"+(endTimestamp - startTimestamp)/1000000 + " ms");
        executorService.shutdown();

        startTimestamp = System.nanoTime();
        Utils.rangSum(array,0,array.length);
        endTimestamp = System.nanoTime();
        System.out.println("sum:"+sum+ " cost:"+(endTimestamp - startTimestamp)/1000000 + " ms");
    }
}
