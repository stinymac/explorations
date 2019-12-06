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

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 简化的 @see {@link java.util.concurrent.ThreadPoolExecutor}
 *
 * 1.线程池初始化
 *
 *   实例化线程池对象及配置线程池参数
 *   private volatile int corePoolSize;
 *   private volatile int maximumPoolSize;
 *   private ThreadFactory threadFactory;
 *   private final BlockingQueue<Runnable> workQueue;
 *   private volatile  long keepAliveTime;
 *   private TimeUnit keepAliveTimeUnit;
 *   private final RejectedExecutionHandler rejectedExecutionHandler;
 *
 * 2.任务提交
 *
 *     工作线程的数量<核心线程数
 *         创建工作线程worker (worker持有新建线程的引用，并将任务直接设置给Worker)
 *         启动新建线程
 *
 *         线程启动后执行worker本身作为线程任务设定的任务，即执行首次创建时设置到worker的任务
 *         或循环从缓冲队列中取任务执行
 *
 *         没有取到任务回收空闲非核心线程或检查是否线程池需要终止
 *
 *     工作线程的数量>=核心线程数
 *
 *         任务进入缓冲队列
 *         并判根据ctl回收空闲线程
 *
 *     缓冲队列满添加非核心工作线程
 *
 *     工作线程数 >= 最大线程数 拒绝任务
 *
 * 3.关闭线程池
 *
 *
 *
 * @auther mac
 * @date 2019-12-06
 */
public class SimpleThreadPool implements SimpleExecutorService {

    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private ThreadFactory threadFactory;
    private final BlockingQueue<Runnable> workQueue;
    private volatile  long keepAliveTime;
    private TimeUnit keepAliveTimeUnit;
    private final RejectedExecutionHandler rejectedExecutionHandler;

    private volatile boolean allowCoreThreadTimeOut;

    private final ReentrantLock mainLock = new ReentrantLock();
    private final Condition termination = mainLock.newCondition();

    /**
     * 线程池中的工作线程
     */
    private final HashSet<Worker> workers = new HashSet<>();

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    // 当前的运行状态
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }

    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }


    private void decrementWorkerCount() {
        do {} while (! compareAndDecrementWorkerCount(ctl.get()));
    }


    public SimpleThreadPool(int corePoolSize, int maximumPoolSize){
        this(corePoolSize,maximumPoolSize,null,null, 0, TimeUnit.MILLISECONDS, null);
    }

    public SimpleThreadPool(int corePoolSize, int maximumPoolSize, ThreadFactory threadFactory, BlockingQueue<Runnable> workQueue, long keepAliveTime, TimeUnit keepAliveTimeUnit, RejectedExecutionHandler rejectedExecutionHandler) {
        if (corePoolSize <= 0 || corePoolSize > maximumPoolSize || keepAliveTime < 0 || keepAliveTimeUnit == null)
            throw new IllegalArgumentException("argument error[corePoolSize:"
                    +corePoolSize+" maximumPoolSize:"
                    +maximumPoolSize+" keepAliveTime:"
                    +keepAliveTime+" keepAliveTimeUnit:"
                    +keepAliveTimeUnit+"]");
        
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTimeUnit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory == null ? new DefaultThreadFactory() : threadFactory;
        this.workQueue = workQueue == null ? new LinkedBlockingQueue<>() : workQueue;
        this.rejectedExecutionHandler = rejectedExecutionHandler == null ? new AbortPolicy() : rejectedExecutionHandler;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    @Override
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        /*
         * Proceed in 3 steps:
         *
         * 1. If fewer than corePoolSize threads are running, try to
         * start a new thread with the given command as its first
         * task.  The call to addWorker atomically checks runState and
         * workerCount, and so prevents false alarms that would add
         * threads when it shouldn't, by returning false.
         *
         * 2. If a task can be successfully queued, then we still need
         * to double-check whether we should have added a thread
         * (because existing ones died since last checking) or that
         * the pool shut down since entry into this method. So we
         * recheck state and if necessary roll back the enqueuing if
         * stopped, or start a new thread if there are none.
         *
         * 3. If we cannot queue task, then we try to add a new
         * thread.  If it fails, we know we are shut down or saturated
         * and so reject the task.
         */
        int c = ctl.get();
        /**
         * 线程数量小于设定的池核心线程数量
         */
        if (workerCountOf(c) < corePoolSize) {
            // 新建worker(持有新建的线程)并启动
            if (addWorker(command, true))
                return;
            // 新建worker失败
            c = ctl.get();
        }
        /**线程池running并任务加入缓冲队列成功*/
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        else if (!addWorker(command, false))
            reject(command);
    }

    public boolean remove(Runnable task) {
        boolean removed = workQueue.remove(task);
        tryTerminate(); // In case SHUTDOWN and now empty
        return removed;
    }

    private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            /**
             * RUNNING:-536870912(MIN)
             * SHUTDOWN:0
             * TIDYING:1073741824
             * TERMINATED:1610612736
             * STOP:536870912(MAX)
             * 检查当前线程池的状态
             *
             * if (rs > SHUTDOWN){
             *     return false;
             * }
             *
             * if (rs == SHUTDOWN && firstTask != null ){
             *     return false;
             * }
             *
             * if (rs == SHUTDOWN && firstTask == null && workQueue.isEmpty() ){
             *     return false;
             * }
             */
            if (rs >= SHUTDOWN &&
                    ! (rs == SHUTDOWN && firstTask == null && ! workQueue.isEmpty()))
                return false;

            for (;;) {
                int wc = workerCountOf(c);
                /**
                 * 线程数量大于等于核心线程数(当新增核心线程时)或最大线程数(当新增非核心线程时)
                 */
                if (wc >= CAPACITY ||
                        wc >= (core ? corePoolSize : maximumPoolSize))
                    return false;
                // worker计数+1
                if (compareAndIncrementWorkerCount(c))
                    // 跳出这两个自旋 即到goto-> boolean workerStarted = false;
                    break retry;
                c = ctl.get();  // Re-read ctl
                if (runStateOf(c) != rs)
                    continue retry;
                // else CAS failed due to workerCount change; retry inner loop
            }
        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            /**
             *  setState(-1); // 禁止中断，直到Worker运行
             *  this.firstTask = firstTask;
             *  this.thread = getThreadFactory().newThread(this);
             */
            w = new Worker(firstTask);
            // 新建的线程
            final Thread t = w.thread;
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();//workers.add(w);
                try {
                    // Recheck while holding lock.
                    // Back out on ThreadFactory failure or if
                    // shut down before lock acquired.
                    int rs = runStateOf(ctl.get());

                    if (rs < SHUTDOWN ||
                            (rs == SHUTDOWN && firstTask == null)) {
                        if (t.isAlive()) // precheck that t is startable
                            throw new IllegalThreadStateException();
                        // worker添加到集合中
                        workers.add(w);
                        /*int s = workers.size();
                        if (s > largestPoolSize)
                            largestPoolSize = s;*/
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                    // 启动新建的线程
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            if (! workerStarted)
                addWorkerFailed(w);
        }
        return workerStarted;
    }

    private void addWorkerFailed(Worker w) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (w != null)
                workers.remove(w);
            decrementWorkerCount();
            tryTerminate();
        } finally {
            mainLock.unlock();
        }
    }

    @Override
    public void shutdown() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            advanceRunState(SHUTDOWN);
            interruptIdleWorkers(false);
            onShutdown(); // hook for ScheduledThreadPoolExecutor
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
    }

    private void advanceRunState(int targetState) {
        for (;;) {
            int c = ctl.get();
            if (runStateAtLeast(c, targetState) ||
                    ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))))
                break;
        }
    }
    void onShutdown() {
    }

    /**
     * 工作线程
     */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {

        Thread thread;
        Runnable firstTask;

        Worker(Runnable firstTask) {
            setState(-1); // 禁止中断，直到Worker运行
            this.firstTask = firstTask;
            /**
             *  worker 本身是一个Runnable
             * 即worker本身需要执行的任务是不断的从缓冲队列中取任务
             * 然后提价给其自身持有的线程来执行任务
             **/
            this.thread = getThreadFactory().newThread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }

        protected boolean isHeldExclusively() {
            return getState() != 0;
        }

        protected boolean tryAcquire(int unused) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        protected boolean tryRelease(int unused) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        public void lock()        { acquire(1); }
        public boolean tryLock()  { return tryAcquire(1); }
        public void unlock()      { release(1); }
        public boolean isLocked() { return isHeldExclusively(); }

        void interruptIfStarted() {
            Thread t;
            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                }
            }
        }
    }

    private void runWorker(Worker worker) {
        // 当前工作线程 - 提交任务时创建并启动的线程 worker.thread
        Thread wt = Thread.currentThread();
        // 工作线程的任务
        Runnable task = worker.firstTask;
        worker.firstTask = null;
        worker.unlock();
        boolean completedAbruptly = true;

        try {
            /**循环从缓冲队列中取得任务执行(创建Worker直接赋予的任务执行后 task == null表示停止执行任务)*/
            while (task != null || (task = getTask()) != null) {
                // 加锁执行任务  -- 状态控制?
                worker.lock();
                // If pool is stopping, ensure thread is interrupted;
                // if not, ensure thread is not interrupted.  This
                // requires a recheck in second case to deal with
                // shutdownNow race while clearing interrupt
                if ((runStateAtLeast(ctl.get(), STOP) || (Thread.interrupted() &&
                                runStateAtLeast(ctl.get(), STOP))) && !wt.isInterrupted())
                    wt.interrupt();

                try {
                    //beforeExecute(wt, task);
                    Throwable thrown = null;
                    try {
                        // 执行任务
                        task.run();
                    } catch (RuntimeException x) {
                        thrown = x; throw x;
                    } catch (Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                        //afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    //worker.completedTasks++;
                    worker.unlock();
                }
            }
            completedAbruptly = false;
        } finally {
            processWorkerExit(worker, completedAbruptly);
        }
    }

    /**
     * worker线程停止执行任务后处理
     *
     * 线程回收 清理  结束线程池任务
     *
     * @param w
     * @param completedAbruptly
     */
    private void processWorkerExit(Worker w, boolean completedAbruptly) {
        if (completedAbruptly) // If abrupt, then workerCount wasn't adjusted
            decrementWorkerCount();

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            //completedTaskCount += w.completedTasks;
            workers.remove(w);
        } finally {
            mainLock.unlock();
        }
        // 结束线程池任务
        tryTerminate();

        int c = ctl.get();
        if (runStateLessThan(c, STOP)) {
            // firstTask == null 退出
            if (!completedAbruptly) {
                int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
                if (min == 0 && ! workQueue.isEmpty())
                    min = 1;
                if (workerCountOf(c) >= min)
                    return; // replacement not needed
            }

            addWorker(null, false);
        }
    }

    final void tryTerminate() {
        for (;;) {
            int c = ctl.get();
            if (isRunning(c) ||
                    runStateAtLeast(c, TIDYING) ||
                    (runStateOf(c) == SHUTDOWN && ! workQueue.isEmpty()))
                return;
            if (workerCountOf(c) != 0) { // Eligible to terminate
                interruptIdleWorkers(ONLY_ONE);
                return;
            }

            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                    try {
                        terminated();
                    } finally {
                        ctl.set(ctlOf(TERMINATED, 0));
                        termination.signalAll();
                    }
                    return;
                }
            } finally {
                mainLock.unlock();
            }
            // else retry on failed CAS
        }
    }

    protected void terminated() { }

    private static final boolean ONLY_ONE = true;

    private void interruptIdleWorkers(boolean onlyOne) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w : workers) {
                Thread t = w.thread;
                if (!t.isInterrupted() && w.tryLock()) {
                    try {
                        t.interrupt();
                    } catch (SecurityException ignore) {
                    } finally {
                        w.unlock();
                    }
                }
                if (onlyOne)
                    break;
            }
        } finally {
            mainLock.unlock();
        }
    }


    /**
     * 工作线程从Queue中取任务
     *
     * @return
     */
    private Runnable getTask() {
        boolean timedOut = false;

        for (;;) {
            /**
             * ctl 初始值为(RUNNING | 0) RUNNING:-536870912
             */
            int c = ctl.get();
            int rs = runStateOf(c);

            /**
             * RUNNING:-536870912(MIN)
             * SHUTDOWN:0
             * TIDYING:1073741824
             * TERMINATED:1610612736
             * STOP:536870912(MAX)
             *
             * 线程池为空WorkerCount -1 (ctl -1)
             */
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount();
                return null;
            }
            /**线程数量*/
            int wc = workerCountOf(c);

            // Are workers subject to culling?
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
            /**
             * 线程数量大于最大值或大于corePoolSize或超时
             * 同时 线程数大于1或缓冲队列为空 等价于:
             *
             * if (wc > maximumPoolSize && workQueue.isEmpty()){
             *     if (compareAndDecrementWorkerCount(c))
             *         return null;
             *     continue;
             * }
             * if (allowCoreThreadTimeOut && wc > 1 ){
             *    if (compareAndDecrementWorkerCount(c))
             *         return null;
             *     continue;
             * }
             * if (wc > corePoolSize && workQueue.isEmpty()){
             *    if (compareAndDecrementWorkerCount(c))
             *         return null;
             *     continue;
             * }
             **/
            if ((wc > maximumPoolSize || (timed && timedOut))
                    && (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c))
                    return null;
                continue;
            }

            try {
                // timed = allowCoreThreadTimeOut || wc > corePoolSize;
                Runnable r = timed ?
                        workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                        workQueue.take();
                if (r != null)
                    return r;
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
        }
    }

    /**
     * 线程创建工厂接口 提供给调用方自定义线程创建
     */
    public interface ThreadFactory {
        Thread newThread(Runnable r);
    }

    class DefaultThreadFactory implements ThreadFactory {

        private final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() +  "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    /**
     * 线程池拒绝策略接口
     */
    public interface RejectedExecutionHandler {
        void rejectedExecution(Runnable r, SimpleThreadPool executor);
    }

    public static class AbortPolicy implements RejectedExecutionHandler {

        public AbortPolicy() { }

        public void rejectedExecution(Runnable r, SimpleThreadPool e) {
            throw new RuntimeException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }

    final void reject(Runnable command) {
        this.rejectedExecutionHandler.rejectedExecution(command, this);
    }
}
