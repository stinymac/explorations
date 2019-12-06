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
 * 对一个大数据量的数组使用分治的思想递归拆分并发求和
 *
 * @auther mac
 * @date 2019-12-04
 */
public class PlainForkRecursiveSumWithMultithreading {
    // 拆分粒度 即最小单元的大小
    private static final int GRAIN_SIZE = 1;

    static class RecursiveSumTask implements Callable<Long> {

        private final int[] array;
        private final int lo;
        private final int hi;
        private final ExecutorService executorService;

        RecursiveSumTask(int[] array, int lo, int hi, ExecutorService executorService) {
            this.executorService = executorService;
            if (array == null || lo < 0 || hi < lo || hi > array.length)
                throw new IllegalArgumentException("error arg");
            this.array = array;
            this.lo = lo;
            this.hi = hi;
        }

        /**
         * 这样的问题在于最终有多少个子任务就需几乎要有多少个线程
         * 否则因为没有线程来执行递归拆分任务上级递归线程不能返回
         *
         * @return
         * @throws Exception
         */
        @Override
        public Long call() throws Exception {
            if (hi - lo <= GRAIN_SIZE) {
                return Utils.rangSum(this.array,this.lo,this.hi);
            }
            else {
                int mid = lo + ((hi - lo)>>1);
                RecursiveSumTask leftTask = new RecursiveSumTask(array,lo,mid,executorService);
                RecursiveSumTask rightTask = new RecursiveSumTask(array,mid+1,hi,executorService);
                Future<Long> leftFuture = executorService.submit(leftTask);
                Future<Long> rightFuture = executorService.submit(rightTask);
                return leftFuture.get() + rightFuture.get();
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int size = 4;
        int[] array = Utils.buildRandomIntArray(size);
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        RecursiveSumTask task = new RecursiveSumTask(array,0,array.length,executorService);
        long startTimestamp = System.nanoTime();
        Future<Long> future =  executorService.submit(task);
        long sum = future.get();
        long endTimestamp = System.nanoTime();
        System.out.println("sum:"+sum+ " cost:"+(endTimestamp - startTimestamp)/1000000 + " ms");
        executorService.shutdown();
    }
}
