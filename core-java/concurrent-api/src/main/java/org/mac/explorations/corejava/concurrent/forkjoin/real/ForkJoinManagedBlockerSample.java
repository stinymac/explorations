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

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 任务出现线程间同步使用 ManagedBlocker
 *
 * @auther mac
 * @date 2019-12-05
 */
public class ForkJoinManagedBlockerSample {

    private  static  final BigInteger RESERVED = BigInteger.valueOf(-1);

    /**
     * 将 fibonacci(n) 的值保留下来
     */
    private  static  class ReservedFibonacciBlocker implements ForkJoinPool.ManagedBlocker {
        private BigInteger result;
        private final int n;
        private final Map<Integer, BigInteger> cache;

        public ReservedFibonacciBlocker(int n, Map<Integer, BigInteger> cache) {
            this.n = n;
            this.cache = cache;
        }

        @Override
        public boolean block() throws InterruptedException {
            synchronized (RESERVED) {
                while (!isReleasable()) {
                    RESERVED.wait();
                }
            }
            return true;
        }

        @Override
        public boolean isReleasable() {
            return (result = cache.get(n)) != RESERVED;
        }
    }

    private static class Fibonacci {

        public BigInteger f(int n) {
            Map<Integer, BigInteger> cache = new ConcurrentHashMap<>();
            cache.put(0, BigInteger.ZERO);
            cache.put(1, BigInteger.ONE);
            return f(n, cache);
        }

        private BigInteger f(int n, Map<Integer, BigInteger> cache) {

            BigInteger result = cache.putIfAbsent(n, RESERVED);

            if (result == null) {

                int half = (n + 1) / 2;

                RecursiveTask<BigInteger> f0_task = new RecursiveTask<BigInteger>() {
                    @Override
                    protected BigInteger compute() {
                        return f(half - 1, cache);
                    }
                };
                f0_task.fork();

                BigInteger f1 = f(half, cache);
                BigInteger f0 = f0_task.join();

                long time = n > 10000 ? System.currentTimeMillis() : 0;
                try {

                    if (n % 2 == 1) {
                        result = f0.multiply(f0).add(f1.multiply(f1));
                    } else {
                        result = f0.shiftLeft(1).add(f1).multiply(f1);
                    }
                    synchronized (RESERVED) {
                        cache.put(n, result);
                        RESERVED.notifyAll();
                    }
                } finally {
                    time = n > 10000 ? System.currentTimeMillis() - time : 0;
                    if (time > 50)
                        System.out.printf("f(%d) took %d%n", n, time);
                }
            } else if (result == RESERVED) {
                try {
                    ReservedFibonacciBlocker blocker = new ReservedFibonacciBlocker(n, cache);
                    ForkJoinPool.managedBlock(blocker);
                    result = blocker.result;
                } catch (InterruptedException e) {
                    throw new CancellationException("interrupted");
                }

            }
            return result;
        }
    }

    public static void main(String[] args) {

        Fibonacci fib = new Fibonacci();

        System.out.println(fib.f(0));
        System.out.println(fib.f(1));
        System.out.println(fib.f(2));
        System.out.println(fib.f(3));
        System.out.println(fib.f(4));
        System.out.println(fib.f(5));
        System.out.println(fib.f(6));
        System.out.println(fib.f(7));
        System.out.println(fib.f(8));
        System.out.println(fib.f(9));
    }
}