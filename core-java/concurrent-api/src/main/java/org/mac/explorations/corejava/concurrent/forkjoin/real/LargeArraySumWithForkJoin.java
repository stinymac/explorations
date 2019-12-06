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

package org.mac.explorations.corejava.concurrent.forkjoin.real;

import org.mac.explorations.corejava.concurrent.forkjoin.Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 使用Fork/join对超大数组求和
 *
 * @auther mac
 * @date 2019-12-05
 */
public class LargeArraySumWithForkJoin {

    // 拆分粒度 即最小单元的大小 -- 使用Fork/Join处理一般来说粒度不应太细
    private static final int GRAIN_SIZE = 100000;

    static class RecursiveSumTask extends RecursiveTask<Long> {

        private final int[] array;
        private final int lo;
        private final int hi;

        RecursiveSumTask(int[] array, int lo, int hi) {
            this.array = array;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected Long compute() {

            if (hi - lo <= GRAIN_SIZE) {
                return Utils.rangSum(this.array,this.lo,this.hi);
            }
            else {
                int mid = lo + ((hi - lo)>>1);
                RecursiveSumTask leftRecursiveSumTask = new RecursiveSumTask(array,lo,mid);
                RecursiveSumTask rightRecursiveSumTask = new RecursiveSumTask(array,mid+1,hi);

                leftRecursiveSumTask.fork();
                rightRecursiveSumTask.fork();

                return leftRecursiveSumTask.join() + rightRecursiveSumTask.join();
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] array = Utils.buildRandomIntArray(200000000);
        long startTimestamp = System.nanoTime();
        long sum = Utils.rangSum(array,0,array.length -1);
        long endTimestamp = System.nanoTime();
        System.out.println("single thread sum:"+sum+ " cost:"+(endTimestamp - startTimestamp)/1000000000D + " s");

        ForkJoinPool forkJoinPool  = new ForkJoinPool(Runtime.getRuntime().availableProcessors() + 1);
        RecursiveSumTask task = new RecursiveSumTask(array,0,array.length-1);
        startTimestamp = System.nanoTime();
        forkJoinPool.submit(task);
        sum = task.get();
        endTimestamp = System.nanoTime();
        System.out.println("    fork join sum:"+sum+ " cost:"+(endTimestamp - startTimestamp)/1000000000D + " s");
    }
}
