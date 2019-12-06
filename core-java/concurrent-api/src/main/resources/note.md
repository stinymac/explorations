==================================================================================
                                  Java并发编程
#一、线程基础介绍

##Java实现多线程的方法

    实现Runnable接口
    
    继承Thread类
    
    以上两种方式都是通过Java API提供的Thread类的run方法来执行任务代码
    public void run() {
        if (target != null) {
            target.run();
        }
    }
    实现Runnable接口 即给Thread的target域赋值，线程执行时回调执行任务代码
    继承Thread类 覆盖重写Thread类的run方法
    
    其他使用线程的方法如:线程池ThreadPoolExecutor,定时器Timer
    匿名内部内，Lamuda表达式实现Runnable或Callable器本质都是为
    线程提供任务代码
    
##线程的启动

    Thread.start() 启动线程
    
    线程准备就绪 可以被调用执行
    
    start()只能执行一次 不能多次调用
    
    public synchronized void start() {
        /**
         * This method is not invoked for the main method thread or "system"
         * group threads created/set up by the VM. Any new functionality added
         * to this method in the future may have to also be added to the VM.
         *
         * A zero status value corresponds to state "NEW".
         */
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

        /* Notify the group that this thread is about to be started
         * so that it can be added to the group's list of threads
         * and the group's unstarted count can be decremented. */
        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }

    private native void start0();

    
##线程的停止

    使用interrupt通知而不是强制
    
    线程通常在任务顺利结束后或任务抛出异常线程会停止
    
    stop thread way 1 :
    
    // 任务中没有对中断的响应
    while (!Thread.currentThread().isInterrupted()) {
        System.out.println("task run...");
    }
    
    stop thread way 2 :
    // 任务中有对中断的响应
    while (!Thread.currentThread().isInterrupted()) {
        System.out.println("task run...");
        try {
            /**
             * sleep 响应中断抛出异常就会同时清除中断状态
             * 如果不恢复中断则这里!Thread.currentThread().isInterrupted()语句失效
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
    
    中断协作停止线程的最佳实践: 优先选择传递中断 如果无法传递应恢复中断
    
    常见的会响应中断的方法:
    
    Object.wait()/wait(long)/wait(long,int)
    Thread.sleep(long)/sleep(long,int)
    Thread.join()/join(long)/join(long,int)
    java.util.concurrent.BlockingQueue.take()/put(E e)
    java.util.concurrent.locks.Lock.lockInterruptibly()
    java.util.concurrent.CountDownLatch.await()
    java.util.concurrent.Cyclicbarrier.await()
    java.util.concurrent.Exchanger.exchange()
    java.nio.channels.InterruptibleChannel的相关方法
    java.nio.channels.Selector的相关方法
    
    stop停止线程的错误:
    
    线程释放锁并戛然而止可能会造成数据的不一致
    
    volatile boolean 信号量停止线程的错误:
    如果线程被阻塞这无法响应信号
    
    while (!canceled) {
        wait();
    }
    
    几个中断相关的方法:
    
    public static boolean interrupted() {
        // 当前线程 并清除中断状态
        return currentThread().isInterrupted(true);
    }
    
    // 调用的线程对象 不清楚中断状态
    public boolean isInterrupted() {
        return isInterrupted(false);
    }
    
    //设置线程的中断信号 
    public void interrupt() {
        if (this != Thread.currentThread())
            checkAccess();

        synchronized (blockerLock) {
            Interruptible b = blocker;
            if (b != null) {
                interrupt0();           // Just to set the interrupt flag
                b.interrupt(this);
                return;
            }
        }
        interrupt0();
    }
    
##线程的生命周期
    @see java.lang.Thread.State
    
    public enum State {
        /**
         * Thread state for a thread which has not yet started.
         */
        NEW,

        /**
         * Thread state for a runnable thread.  A thread in the runnable
         * state is executing in the Java virtual machine but it may
         * be waiting for other resources from the operating system
         * such as processor.
         */
        RUNNABLE,

        /**
         * Thread state for a thread blocked waiting for a monitor lock.
         * A thread in the blocked state is waiting for a monitor lock
         * to enter a synchronized block/method or
         * reenter a synchronized block/method after calling {@link Object#wait() Object.wait}.
         */
        BLOCKED,

        /**
         * Thread state for a waiting thread.
         * A thread is in the waiting state due to calling one of the
         * following methods:
         * <ul>
         *   <li>{@link Object#wait() Object.wait} with no timeout</li>
         *   <li>{@link #join() Thread.join} with no timeout</li>
         *   <li>{@link LockSupport#park() LockSupport.park}</li>
         * </ul>
         *
         * <p>A thread in the waiting state is waiting for another thread to
         * perform a particular action.
         *
         * For example, a thread that has called <tt>Object.wait()</tt>
         * on an object is waiting for another thread to call
         * <tt>Object.notify()</tt> or <tt>Object.notifyAll()</tt> on
         * that object. A thread that has called <tt>Thread.join()</tt>
         * is waiting for a specified thread to terminate.
         */
        WAITING,

        /**
         * Thread state for a waiting thread with a specified waiting time.
         * A thread is in the timed waiting state due to calling one of
         * the following methods with a specified positive waiting time:
         * <ul>
         *   <li>{@link #sleep Thread.sleep}</li>
         *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
         *   <li>{@link #join(long) Thread.join} with timeout</li>
         *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
         *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
         * </ul>
         */
        TIMED_WAITING,

        /**
         * Thread state for a terminated thread.
         * The thread has completed execution.
         */
        TERMINATED;
    }

    
         NEW                                                              
          |                                                                        
          |  
      Thread.start()
          |                                                             
          |                      进入synchronized修饰的代码块                            
          |         |-------------------------------------------------------------------- Blocked                                                      
          |         |  ------------------------------------------------------------------
          |         |  |  获得monitor锁
          |         |  |
          |         |  |
          |         /  /                                                   
          |        /  /
          |       /  /   
          |      /  / 
          |     /  /
          |    /  /
          |   /  /
          v  /  /   Object.wait() /Thread.join()/LockSupport.park()
          RUNNABLE -------------------------------------------------------------> WAITING
          |  \  \    <----------------------------------------------------------
          |   \  \   Object.notify() /Object.notifyAll()/LockSupport.unpark()                     
          |    \  \                               
          |     \  \ 
          |      \  \
          |       \  \
          |        \  \
          |         \  \
          |         |   |
          |         |   | Thread.sleep()/Object.wait(time) /Thread.join(time)
          |         |   | LockSupport.parkNanos(time)/LockSupport.parkuNTIL(time)
          |         |   --------------------------------------------------------------------->TIMED_WAITING
          |         |------------------------------------------------------------------------
          |           等待时间到/Object.notify() /Object.notifyAll()/LockSupport.unpark()
          |
      执行完成 
          |
          V
        TERMINATED                       


##Java API中Thread类方法和Object类中线程相关方法详解

    Thread:
        
        sleep
        
            只想让线程在预期的时间执行，其他时候不要占用CPU资源
            
            不释放锁和wait不同
                
            响应中断
            
            抛出InterruptedException
            
            清除中断状态
            
            sleep方法可以让线程进入Waiting状态，并且不占用CPU资源，但是不释放锁，直到规定时间后再执行，休眠期间如果被中断，会抛出异常并清除中断状态。
        
        join --- @see org.mac.explorations.corejava.concurrent.basics.thread.methods.JoinPrinciple
        
        yeid
        currentThread
        start run
        interrupt
        
    Object:
        
        wait
            wait直到以下4种情况之一发生时，才会被唤醒
            
            另一个线程调用这个对象的notify()方法且刚好被唤醒的是本线程；
            
            另一个线程调用这个对象的notifyAll()方法；
            
            过了wait(long timeout)规定的超时时间，如果传入0就是永久等待；
            
            线程自身调用了interrupt()
            
        notify
        notifyAll
        
            notify方法只应该被拥有该对象的monitor的线程调用
            
            一旦线程被唤醒，线程便会从对象的“等待线程集合”中被移除，所以可以重新参与到线程调度当中
            
            要等刚才执行notify的线程退出被synchronized保护的代码并释放monitor

##Java API中Thread类的属性
   
    线程-tid
        
         /* For generating thread ID */
         private static long threadSeqNumber;
         
         tid = nextThreadID();
         
         private static synchronized long nextThreadID() {
             return ++threadSeqNumber;
         }   
    
    线程名字-name
    
         public Thread() {
                init(null, null, "Thread-" + nextThreadNum(), 0);
         }
         
          /* For autonumbering anonymous threads. */
          private static int threadInitNumber;
             private static synchronized int nextThreadNum() {
             return threadInitNumber++;
          }
    
    守护线程-daemon
    
        给用户线程提供服务
      
        线程类型默认继承自父线程
        
        不影响JVM退出
    
    优先级-priority
    
        10个级别
        
        默认5
        
        程序设计不应依赖于优先级
        
        不同操作系统不一样
        
        优先级会被操作系统改变

##线程中异常处理

    java.lang.ThreadGroup#uncaughtException(Thread t, Throwable e):
    
    public void uncaughtException(Thread t, Throwable e) {
        if (parent != null) {
            parent.uncaughtException(t, e);
        } else {
            Thread.UncaughtExceptionHandler ueh =
                Thread.getDefaultUncaughtExceptionHandler();
            if (ueh != null) {
                ueh.uncaughtException(t, e);
            } else if (!(e instanceof ThreadDeath)) {
                System.err.print("Exception in thread \""
                                 + t.getName() + "\" ");
                e.printStackTrace(System.err);
            }
        }
    }

##多线程带来的问题分析
    
    线程安全
    
        当多个线程访问某个类时，不管运行时环境采用何种调度方式或者这些线程如何交替执行
        并且在主调代码中不需要任何额外的同步或协同，类都能表现出正确的行为，那么这个类
        是线程安全的类。
    
    什么情况下会出现线程安全问题
        
        数据争用
        死锁等活跃性问题（包括死锁、活锁、饥饿） 
        对象发布和初始化的时候的安全问题   
         
    需要考虑线程安全的情况
    
        访问共享的变量或资源，会有并发风险，比如对象的属性、静态变量、共享缓存、数据库等
        
        所有依赖时序的操作，即使每一步操作都是线程安全的，还是存在并发问题：
        
            # read-modify-write操作：一个线程读取了一个共享数据，并在此基础上更新该数据。
        
            # check-then-act操作：一个线程读取了一个共享数据，并在此基础上决定其下一个的操作
        
        不同的数据之间存在捆绑关系的时候
        
            # IP和端口号--修改需要原子操作
        
        使用其他类的时候，如果类没有声明是线程安全的，那么大概率会存在并发问题
        
    性能
    
        服务响应慢、吞吐量低、资源消耗（例如内存）过高等 虽然不是结果错误，但依然危害巨大 引入多线程不能本末倒置
        
        为什么多线程会带来性能问题
        
            调度：上下文切换
        
            什么是上下文切换？
        
                上下文切换可以认为是内核（操作系统的核心）在 CPU 上对于进程（包括线程）进行以下的活动：
                （1）挂起一个进程，将这个进程在 CPU 中的状态（上下文）存储于内存中的某处，
                （2）在内存中检索下一个进程的上下文并将其在 CPU 的寄存器中恢复，
                （3）跳转到程序计数器所指向的位置（即跳转到进程被中断时的代码行），以恢复该进程。
        
                何时会导致密集的上下文切换--频繁地竞争锁，或者由于IO读写等原因导致频繁阻塞
            
                缓存开销 --CPU重新缓存
        
            协作：内存同步
        
                Java内存模型-为了数据的正确性，同步手段往往会使用禁止编译器优化、使CPU内的缓存失效
                        
###死锁

    什么是死锁？
    当两个（或更多）线程（或进程）相互持有对方所需要的资源，又不主动释放，
    导致所有人都无法继续前进，导致程序陷入无尽的阻塞，这就是死锁。
    
    死锁的必要条件
    
    互斥条件
    
    请求与保持条件
    
    不剥夺条件
    
    循环等待条件
    
    修复策略
    
    避免策略：哲学家就餐的换手方案、转账换序方案
    
    检测与恢复策略：一段时间检测是否有死锁，如果有就剥夺某一个资源，来打开死锁
    
    实际工程中如何避免死锁？
    
    设置超时时间--Lock的tryLock(long timeout, TimeUnit unit)(synchronized不具备超时锁的能力)
    
        造成超时的可能性多：发生了死锁、线程陷入死循环、线程执行很慢
        获取锁失败：打日志、发报警邮件、重启等
    
    使用并发类而不是自己设计锁
    
        ConcurrentHashMap、ConcurrentLinkedQueue、AtomicBoolean等 
        实际应用中java.util.concurrent.atomic十分有用，简单方便且效率比使用Lock更高
        多用并发集合少用同步集合，并发集合比同步集合的可扩展性更好
        
    避免锁的嵌套：MustDeadLock类   
    
    分配资源前先看能不能收回来：银行家算法
    
#二、Java内存模型(JMM)

##什么是JMM
    
    Java Memory Model是一组规范，需要各个JVM的实现来遵守JMM规范，以便于开发者可以利用这些规范，
    更方便地开发多线程程序。
    如果没有这样的一个JMM内存模型来规范，那么很可能经过了不同JVM的不同规则的重排序之后，
    导致不同的虚拟机上运行的结果不一样。
    
    volatile、synchronized、Lock等的原理都是JMM
    
    最重要的3点内容
    
        重排序
            什么是重排序--实际执行顺序和代码在java文件中的顺序不一致
            
            重排序的好处：提高处理速度
          
            重排序情况
            
                编译器优化
            
                指令重排序
            
                    CPU 的优化行为，和编译器优化很类似，是通过乱序执行的技术，来提高执行效率。
                    所以就算编译器不发生重排，CPU 也可能对指令进行重排。
                    
            happens-before relationship
                
                1 代码的执行顺序，编写在前面的发生在编写在后面的
                2 unlock必须发生在lock之后
                3 volatile修饰的变量，对一个变量的写操作先于对该变量的读操作。
                4 传递规则，操作A先于B，B先于C，那么A肯定先于C
                5 线程启动规则，start方法肯定先于线程run
                6 线程中断规则，interrupt这个动作，必须发生在捕获该动作之前
                7 对象销毁规则，初始化必须发生在finalize之前
                8 线程终结规则，所有的操作都发生在线程死亡之前
    
        内存可见性
        
            CPU有多级缓存，导致读的数据过期
            
            高速缓存的容量比主内存小，但是速度仅次于寄存器，所以在CPU和主内存之间就多了Cache层
            
            线程间对于共享变量的可见性问题不是直接由多核引起的，而是由多缓存引起的。
            
            如果所有个核心都只用一个缓存，那么也就不存在内存可见性问题了。
            
            每个核心都会将自己需要的数据读到独占缓存中，数据修改后也是写入到缓存中，然后等待刷入到主存中。
            所以会导致有些核心读取的值是一个过期的值。
            
            JMM的抽象：主内存和本地内存
            
            什么是主内存和本地内存
            
            Java 作为高级语言，屏蔽了CPU cache等底层细节，用 JMM 定义了一套读写内存数据的规范，
            不再需要关心一级缓存和二级缓存的问题，但是，JMM 抽象了主内存和本地内存的概念。
            
            主内存和本地内存的关系
            
            1.	所有的变量都存储在主内存中，同时每个线程也有自己独立的工作内存，工作内存中的变量内容是主内存中的拷贝
            
            2.	线程不能直接读写主内存中的变量,而是只能操作自己工作内存中的变量，然后再同步到主内存中
            
            3.	主内存是多个线程共享的，但线程间不共享工作内存,如果线程间需要通信，必须借助主内存中转来完成
            
            所有的共享变量存在于主内存中，每个线程有自己的本地内存，而且线程读写共享数据也是通过本地内存交换的，所以才导致了可见性问题。
            
            解决共享变量缓存一致性的两种方法:
            1.总线锁
            2.高速缓存一致性协议(MESI)
                当CPU写入数据时发现该变量被共享(其他CPU中存有该变量的副本) 则发送一个信号使其失效
                其他CPU访问该变量时重新从主存中读取
    
        原子性
            对基本类型的数据的读取和赋值是原子性的,但64位的数据可能不是.
            
            i = 10;(是)
            i = x; (非)
            
    volatile
    
    1.	volatile 修饰符适用于以下场景：某个属性被多个线程共享，其中有一个线程修改了此属性，
        其他线程可以立即得到修改后的值，比如boolean flag。
    
    2.	volatile 属性的读写操作都是无锁的，它不能替代 synchronized，因为它没有提供原子性和互斥性。
        因为无锁，不需要花费时间在获取锁和释放锁上，所以说它是低成本的。
    
    3.	volatile 只能作用于属性，用 volatile 修饰属性，这样 compilers 就不会对这个属性做指令重排序。
    
    4.	volatile 提供了可见性，任何一个线程对其的修改将立马对其他线程可见。volatile 属性不会被线程缓存，始终从主存中读取。
    
    5.	volatile 提供了 happens-before 保证，对 volatile 变量 v 的写入 happens-before 所有其他线程后续对 v 的读操作。
    
    synchronized
    
    synchronized不仅防止了一个线程在操作某对象时收到其他线程的干扰，同时还保证了修改好之后，
    可以立即被其他线程所看到。（因为如果其他线程看不到，那也会有线程安全问题）
    
#三、synchronized关键字--Java内置锁

    synchronized内置锁是一种对象锁(锁的是对象而非引用)，作用粒度是对象，
    可以用来实现对临界资源的同步互斥访问，是可重入的。
    
    加锁的方式：
        同步实例方法，锁是当前实例对象 
        同步类方法，锁是当前类对象
        同步代码块，锁是括号里面的对象
    
    synchronized底层原理 
        synchronized是基于JVM内置锁实现，通过内部对象Monitor(监视器锁)实现，
        基于进入与退出Monitor对象实现方法与代码块同步，监视器锁的实现依赖底层
        操作系统的Mutex lock（互斥锁）实现，它是一个重量级锁性能较低。
        
        JVM内置锁在1.5之后版本做了重大的优化，如锁粗化（Lock Coarsening）、
        锁消除（Lock Elimination）、轻量级锁（Lightweight Locking）、
        偏向锁（Biased Locking）、适应性自旋（Adaptive Spinning）等技术来减少锁操作的开销，
        内置锁的并发性能已经基本与Lock持平。 
        
        synchronized关键字被编译成字节码后会被翻译成monitorenter 和 monitorexit 两条指令
        分别在同步块逻辑代码的起始位置与结束位置。
        
    对象是如何记录锁状态？
    
    对象的内存布局
        HotSpot虚拟机中，对象在内存中存储的布局可以分为三块区域：
        对象头 （Header）、实例数据（Instance Data）和对齐填充（Padding）
        
        对象头：比如 hash码，对象所属的年代，对象锁，锁状态标志，偏向锁（线程）ID，偏向时间，数组长度（数组对象）等 
        实例数据：即创建对象时，对象中成员变量，方法等 
        对齐填充：对象的大小必须是8字节的整数倍
        
    对象头
        HotSpot虚拟机的对象头包括两部分信息，第一部分是“Mark Word”，用于存储对象自身的运行时数据，
        如哈希码（HashCode）、GC分代年龄、锁状态标志、线程持有的锁、偏向线程ID、偏向时间戳等等，
        这部分数据的长度在32位和64位的虚拟机中分别为32个和64个Bits，官方称它为“Mark Word”。
        
        对象需要存储的运行时数据很多，其实已经超出了32、64位Bit结构所能记录的限度，
        但是对象头信息是与对象自身定义的数据无关的额外存储成本，考虑到虚拟机的空间效率，
        Mark Word被设计成一个非固定的数据结构以便在极小的空间内存储尽量多的信息
        它会根据对象的状态复用自己的存储空间。例如在32位的HotSpot虚拟机中对象未被锁定的状态下，
        Mark Word的32个Bits空间中的25Bits用于存储对象哈希码（HashCode），4Bits用于存储对象
        分代年龄，2Bits用于存储锁标志位，1Bit固定为0，
        在其他状态（轻量级锁定、重量级锁定、GC标记、可偏向）下对象的存储内容如下所示。
        
        25bit        4bit             1bit                  2bit
        对象哈希码    对象分代年龄      是否偏向锁             锁标志
        
        如果对象是数组类型，则需要三个机器码，因为JVM虚拟机可以通过Java对象的元数据信息
        确定Java对象的大小，但是无法从数组的元数据来确认数组的大小，所以用一块来记录数组长度。
        
    锁的膨胀升级过程 
        
        锁的状态总共有四种，无锁状态、偏向锁、轻量级锁和重量级锁。
        随着锁的竞争，锁可以从偏向锁升级到轻量级锁，再升级的重量级锁，
        但是锁的升级是单向的，也就是说只能从低到高升级，不会出现锁的降级。
    
    偏向锁 
    
        偏向锁是Java6之后加入的新锁，它是一种针对加锁操作的优化手段，
        在大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，
        因此为了减少同一线程获取锁(会涉及到一些CAS操作耗时)的代价而引入偏向锁。
        偏向锁的核心思想是，如果一个线程获得了锁，那么锁就进入偏向模式，
        此时Mark Word 的结构也变为偏向锁结构，当这个线程再次请求锁时，
        无需再做任何同步操作，即获取锁的过程，这样就省去了大量有关锁申请的操作，
        从而也就提供程序的性能。所以，对于没有锁竞争的场合，偏向锁有很好的优化效果，
        毕竟极有可能连续多次是同一个线程申请相同的锁。
        但是对于锁竞争比较激 烈的场合，偏向锁就失效了，
        因为这样场合极有可能每次申请锁的线程都是不相同的，
        因此这种场合下不应该使用偏向锁，否则会得不偿失，需要注意的是，偏向锁失败后，
        并不会立即膨胀为重量级锁，而是先升级为轻量级锁。
        
    轻量级锁 
    
        倘若偏向锁失败，虚拟机并不会立即升级为重量级锁，它还会尝试使用一种称为轻量级锁的优化手段
        (1.6之后加入的)，此时Mark Word 的结构也变为轻量级锁的结构。轻量级锁能够提升程序性能的
        依据是“对绝大部分的锁，在整个同步周期内都不存在竞争”，轻量级锁所适应的场景是线程交替执行
        同步块的场合，如果存在同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁。
         
    自旋锁
        轻量级锁失败后，虚拟机为了避免线程真实地在操作系统层面挂起，还会进行一项称为自旋锁的优化手段。
        这是基于在大多数情况下，线程持有锁的时间都不会太长，如果直接挂起操作系统层面的线程可能会得不偿失，
        毕竟操作系统实现线程之间的切换时需要从用户态转换到核心态，这个状态之间的转换需要相对比较长的时间，
        时间成本相对较高，因此自旋锁会假设在不久将来，当前的线程可以获得锁，因此虚拟机会让当前想要获取锁
        的线程做几个空循环(这也是称为自旋的原因)，一般不会太久，可能是50个循环或100循环，在经过若干次循环后，
        如果得到锁，就顺利进入临界区。如果还不能获得锁，那就会将线程在操作系统层面挂起，
        这就是自旋锁的优化方式，这种方式确实也是可以提升效率的。 最后没办法也就只能升级为重量级锁了。 
        
    锁消除 
        消除锁是虚拟机另外一种锁的优化，这种优化更彻底，Java虚拟机在JIT编译时
        (可以简单理解为当某段代码即将第一次被执行时进行编译，又称即时编 译)，
        通过对运行上下文的扫描，去除不可能存在共享资源竞争的锁，通过这种方式消除没有必要的锁，
        可以节省毫无意义的请求锁时间，如下StringBuffer的append是一个同步方法，
        但是在某个方法中的StringBuffer属于一个局部变量， 并且不会被其他线程所使用，
        因此StringBuffer不可能存在共享资源竞争的情景，JVM会自动将其锁消除。
        
    偏向锁默认开启
    开启偏向锁：-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
    关闭偏向锁：-XX:-UseBiasedLocking

    
    逃逸分析 
    
        使用逃逸分析，编译器可以对代码做如下优化： 
        一、同步省略。如果一个对象被发现只能从一个线程被访问到，
            那么对于这个对象的操作可 以不考虑同步。 
        二、将堆分配转化为栈分配。如果一个对象在子程序中被分配，如果指向该对象的指针永远不会逃逸，
            对象可能是栈分配的候选，而不是堆分配。  
        三、分离对象或标量替换。有的对象可能不需要作为一个连续的内存结构存在也可以被访问到，
           那么对象的部分（或全部）可以不存储在内存，而是存储在CPU寄存器中。 
           
    是不是所有的对象和数组都会在堆内存分配空间？ 不一定(逃逸分析--将堆分配转化为栈分配)
    
    Java代码运行时，通过JVM参数可指定是否开启逃逸分析， 
        ­ XX:+DoEscapeAnalysis ： 表示开启逃逸分析 
        ­XX:­DoEscapeAnalysis ： 表示关闭逃逸分析 
        
    从JDK1.7开始已经默认开始逃逸分析，如需关闭，需要指定­XX:­ DoEscapeAnalysis
    
#四、Java并发工具类-JUC

    AbstractQueuedSynchronizer(AQS)
    
        AQS定 义了一套多线程访问共享资源的同步器框架，是一个依赖状态(state)的同步器。
        
        AQS具备特性 
        
            阻塞等待队列 
            共享/独占 
            公平/非公平 
            可重入 
            允许中断
    Java.concurrent.util当中同步器的实现如Lock,Latch,Barrier等，
    都是基于AQS框架实现,一般通过定义内部类Sync继承AQS将同步器所有
    调用都映射到Sync对应的方法. AQS内部维护属性volatile int state (32位) 
    state表示资源的可用状态 
    
        State三种访问方式 getState()、setState()、compareAndSetState() 
    
        AQS定义两种资源共享方式 
            Exclusive-独占，只有一个线程能执行，如ReentrantLock 
            Share-共享，多个线程可以同时执行，如Semaphore/CountDownLatch 
            
        AQS定义两种队列 
            同步等待队列 
            条件等待队列 
            
        不同的自定义同步器争用共享资源的方式也不同。
        自定义同步器在实现时只需要实现共享资源state的获取与释放方式即可，
        至于具体线程等待队列的维护 （如获取资源失败入队/唤醒出队等），AQS已经在顶层实现好了。
        自定义同步器 实现时主要实现以下几种方法： 
            isHeldExclusively()：该线程是否正在独占资源。只有用到 condition才需要去实现它。 
            tryAcquire(int)：独占方式。尝试获取资源，成功则返回true，失败 则返回false。 
            tryRelease(int)：独占方式。尝试释放资源，成功则返回true，失败 则返回false。 
            tryAcquireShared(int)：共享方式。尝试获取资源。负数表示失败； 0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。 
            tryReleaseShared(int)：共享方式。尝试释放资源，如果释放后允许 唤醒后续等待结点返回true，否则返回false。 
        
        同步等待队列 
        
            AQS当中的同步等待队列也称CLH队列，CLH队列是Craig、Landin、 Hagersten
            三人发明的一种基于双向链表数据结构的队列，是FIFO先入先出线程等待队列，
            Java中的CLH队列是原CLH队列的一个变种,线程由原自旋机制改为阻塞机制。
            
        条件等待队列
         
            Condition是一个多线程间协调通信的工具类，使得某个或者某些线程一起等待某个条件（Condition）,
            只有当该条件具备时，这些等待线程才会被唤醒，从而重新争夺锁
            
##分析AbstractQueuedSynchronizer源码
    
    //-AQS的队列节点
    
    static final class Node {
        /**
         * 标记节点未共享模式
         * */
        static final Node SHARED = new Node();
        /**
         *  标记节点为独占模式
         */
        static final Node EXCLUSIVE = null;

        /**
         * 在同步队列中等待的线程等待超时或者被中断，需要从同步队列中取消等待
         * */
        static final int CANCELLED =  1;
        /**
         *  后继节点的线程处于等待状态，而当前的节点如果释放了同步状态或者被取消，
         *  将会通知后继节点，使后继节点的线程得以运行。
         */
        static final int SIGNAL    = -1;
        /**
         *  节点在等待队列中，节点的线程等待在Condition上，当其他线程对Condition调用了signal()方法后，
         *  该节点会从等待队列中转移到同步队列中，加入到同步状态的获取中
         */
        static final int CONDITION = -2;
        /**
         * 表示下一次共享式同步状态获取将会被无条件地传播下去
         */
        static final int PROPAGATE = -3;

        /**
         * 标记当前节点的信号量状态 (1,0,-1,-2,-3)5种状态
         * 使用CAS更改状态，volatile保证线程可见性，高并发场景下，
         * 即被一个线程修改后，状态会立马让其他线程可见。
         */
        volatile int waitStatus;

        /**
         * 前驱节点，当前节点加入到同步队列中被设置
         */
        volatile Node prev;

        /**
         * 后继节点
         */
        volatile Node next;

        /**
         * 节点同步状态的线程
         */
        volatile Thread thread;

        /**
         * 等待队列中的后继节点，如果当前节点是共享的，那么这个字段是一个SHARED常量，
         * 也就是说节点类型(独占和共享)和等待队列中的后继节点共用同一个字段。
         */
        Node nextWaiter;

        /**
         * Returns true if node is waiting in shared mode.
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * 返回前驱节点
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }
    
     /**
      * 指向同步等待队列的头节点
      */
     private transient volatile Node head;
    
     /**
      * 指向同步等待队列的尾节点
      */
     private transient volatile Node tail;
    
     /**
      * 同步资源状态
      */
     private volatile int state;
     
     // state的CAS操作
     unsafe.compareAndSwapInt(this, stateOffset, expect, update);
     
     
     // - ReentrantLock内部对AbstractQueuedSynchronizer的实现
     
     /**
      * 内部调用AQS的动作，都基于该成员属性实现
      */
     private final Sync sync;
 
     /**
      * ReentrantLock锁同步操作的基础类,继承自AQS框架.
      * 该类有两个继承实现类，1、NonfairSync 非公平锁，2、FairSync公平锁
      */
     abstract static class Sync extends AbstractQueuedSynchronizer {
         private static final long serialVersionUID = -5179523762034025860L;
 
         /**
          * 加锁的具体行为由子类实现
          */
         abstract void lock();
 
         /**
          * 尝试获取非公平锁
          */
         final boolean nonfairTryAcquire(int acquires) {
             //acquires = 1
             final Thread current = Thread.currentThread();
             int c = getState();
             /**
              * 不需要判断同步队列（CLH）中是否有排队等待线程
              * 判断state状态是否为0，不为0可以加锁
              */
             if (c == 0) {
                 //unsafe操作，cas修改state状态
                 if (compareAndSetState(0, acquires)) {
                     //独占状态锁持有者指向当前线程
                     setExclusiveOwnerThread(current);
                     return true;
                 }
             }
             /**
              * state状态不为0，判断锁持有者是否是当前线程，
              * 如果是当前线程持有 则state+1
              */
             else if (current == getExclusiveOwnerThread()) {
                 int nextc = c + acquires;
                 if (nextc < 0) // overflow
                     throw new Error("Maximum lock count exceeded");
                 setState(nextc);
                 return true;
             }
             //加锁失败
             return false;
         }
 
         /**
          * 释放锁
          */
         protected final boolean tryRelease(int releases) {
             int c = getState() - releases;
             if (Thread.currentThread() != getExclusiveOwnerThread())
                 throw new IllegalMonitorStateException();
             boolean free = false;
             if (c == 0) {
                 free = true;
                 setExclusiveOwnerThread(null);
             }
             setState(c);
             return free;
         }
 
         /**
          * 判断持有独占锁的线程是否是当前线程
          */
         protected final boolean isHeldExclusively() {
             return getExclusiveOwnerThread() == Thread.currentThread();
         }
 
         //返回条件对象
         final ConditionObject newCondition() {
             return new ConditionObject();
         }
 
 
         final Thread getOwner() {
             return getState() == 0 ? null : getExclusiveOwnerThread();
         }
 
         final int getHoldCount() {
             return isHeldExclusively() ? getState() : 0;
         }
 
         final boolean isLocked() {
             return getState() != 0;
         }
 
         /**
          * Reconstitutes the instance from a stream (that is, deserializes it).
          */
         private void readObject(java.io.ObjectInputStream s)
                 throws java.io.IOException, ClassNotFoundException {
             s.defaultReadObject();
             setState(0); // reset to unlocked state
         }
     }
 
     /**
      * 非公平锁
      */
     static final class NonfairSync extends Sync {
         private static final long serialVersionUID = 7316153563782823691L;
         /**
          * 加锁行为
          */
         final void lock() {
             /**
              * 第一步：直接尝试加锁
              * 与公平锁实现的加锁行为一个最大的区别在于，此处不会去判断同步队列(CLH队列)中
              * 是否有排队等待加锁的节点，上来直接加锁（判断state是否为0,CAS修改state为1）
              * ，并将独占锁持有者 exclusiveOwnerThread 属性指向当前线程
              * 如果当前有人占用锁，再尝试去加一次锁
              */
             if (compareAndSetState(0, 1))
                 setExclusiveOwnerThread(Thread.currentThread());
             else
                 //AQS定义的方法,加锁
                 acquire(1);
         }
 
         /**
          * 父类AbstractQueuedSynchronizer.acquire()中调用本方法
          */
         protected final boolean tryAcquire(int acquires) {
             return nonfairTryAcquire(acquires);
         }
     }
 
     /**
      * 公平锁
      */
     static final class FairSync extends Sync {
         private static final long serialVersionUID = -3000897897090466540L;
 
         final void lock() {
             acquire(1);
         }
 
         /**
          * 重写aqs中的方法逻辑
          * 尝试加锁，被AQS的acquire()方法调用
          */
         protected final boolean tryAcquire(int acquires) {
             final Thread current = Thread.currentThread();
             int c = getState();
             if (c == 0) {
                 /**
                  * 与非公平锁中的区别，需要先判断队列当中是否有等待的节点
                  * 如果没有则可以尝试CAS获取锁
                  */
                 if (!hasQueuedPredecessors() &&
                         compareAndSetState(0, acquires)) {
                     //独占线程指向当前线程
                     setExclusiveOwnerThread(current);
                     return true;
                 }
             }
             else if (current == getExclusiveOwnerThread()) {
                 int nextc = c + acquires;
                 if (nextc < 0)
                     throw new Error("Maximum lock count exceeded");
                 setState(nextc);
                 return true;
             }
             return false;
         }
     }
     
     //ReentrantLock默认构造非公平锁 public ReentrantLock() {sync = new NonfairSync();}
     
     =========================================ReentrantLock(独占模式)获取锁=========================================
     //ReentrantLock 获取锁-lock() - 非公平锁
     
     final void lock() {
         /**
          * 第一步：直接尝试加锁
          * 与公平锁实现的加锁行为一个最大的区别在于，此处不会去判断同步队列(CLH队列)中
          * 是否有排队等待加锁的节点，而直接加锁（判断state是否为0,CAS修改state为1）
          * 并将独占锁持有者 exclusiveOwnerThread 属性指向当前线程
          * 如果当前有人占用锁，再尝试去加一次锁
          */
         if (compareAndSetState(0, 1))
             setExclusiveOwnerThread(Thread.currentThread());
         else
            //AQS定义的方法,加锁
             acquire(1);
     }
     
     //ReentrantLock 获取锁-lock() - 公平锁
     final void lock() {
         acquire(1); // 即调用AQS的acquire()
     }
     
     
     // 非公平锁尝试获取锁
     final boolean nonfairTryAcquire(int acquires) {
         //acquires = 1
         final Thread current = Thread.currentThread();
         // 获取锁状态 state状态是否为0，为0可以加锁
         int c = getState();
         
         /**
          * 不需要判断同步队列（CLH）中是否有排队等待线程
          */
         if (c == 0) {
             //unsafe操作，cas修改state状态
             if (compareAndSetState(0, acquires)) {
                 //独占状态锁持有者指向当前线程
                 setExclusiveOwnerThread(current);
                 return true;
             }
         }
         /**
          * state状态不为0，判断锁持有者是否是当前线程，
          * 如果是当前线程持有 则state+1
          */
         else if (current == getExclusiveOwnerThread()) {
             int nextc = c + acquires;
             if (nextc < 0) // overflow
                 throw new Error("Maximum lock count exceeded");
             setState(nextc);
             return true;
         }
         //加锁失败
         return false;
     }
     
     // 公平锁尝试获取锁
     protected final boolean tryAcquire(int acquires) {
         final Thread current = Thread.currentThread();
         int c = getState();
         if (c == 0) {
             /**
              * 与非公平锁中的区别，需要先判断队列当中是否有等待的节点
              * 如果没有则可以尝试CAS获取锁
              */
             if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
                 //独占线程指向当前线程
                 setExclusiveOwnerThread(current);
                 return true;
             }
         }
         else if (current == getExclusiveOwnerThread()) {
             int nextc = c + acquires;
             if (nextc < 0)
                 throw new Error("Maximum lock count exceeded");
             setState(nextc);
             return true;
         }
         return false;
     }
     
     
     //AQS的获取方法
     public final void acquire(int arg) {
         // 尝试获取锁失败 线程进入队列排队 并恢复中断
         if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
             selfInterrupt();
     }
     
     // 为当前线程创建节点 并入队
     private Node addWaiter(Node mode) {
         Node node = new Node(Thread.currentThread(), mode);
         // Try the fast path of enq; backup to full enq on failure
         Node pred = tail;
         if (pred != null) {  
             node.prev = pred;
             // 没有并发竞争 设置新节点为尾节点成功 直接返回
             if (compareAndSetTail(pred, node)) {
                 pred.next = node;
                 return node;
             }
         }
         // 队列为空或设置节点为尾节点失败 则自旋入队
         enq(node);
         return node;
     }
     // 节点自旋入队
     private Node enq(final Node node) {
         for (;;) {
             Node t = tail;
             if (t == null) { // Must initialize
                 // 初始化头节点(哑节点)
                 if (compareAndSetHead(new Node()))
                     tail = head;
             } else {
                 node.prev = t;
                 if (compareAndSetTail(t, node)) {
                     t.next = node;
                     return t;
                 }
             }
         }
     }
     // 节点自旋入队成功后 节点的前驱节点如果是头节点 头节点尝试获取锁
     final boolean acquireQueued(final Node node, int arg) {
         boolean failed = true;
         try {
             boolean interrupted = false;
             for (;;) {
                 final Node p = node.predecessor();
                 //前驱节点如果是头节点尝试获取锁
                 if (p == head && tryAcquire(arg)) {
                     /**
                      * private void setHead(Node node) {
                      *    head = node;
                      *    node.thread = null;
                      *    node.prev = null;
                      * }
                      * 
                      * 被唤醒的线程获取锁成功后线程节点出队 
                      */
                     setHead(node);
                     p.next = null; // help GC
                     failed = false;
                     return interrupted;
                 }
                 //前驱节点如果不是头节点或获取锁失败 阻塞当前线程
                 if (shouldParkAfterFailedAcquire(p, node) &&
                     parkAndCheckInterrupt())
                     interrupted = true;
             }
         } finally {
             if (failed)
                 cancelAcquire(node);
         }
     }
     
     
     
     private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
         int ws = pred.waitStatus;
         //Node.SIGNAL = -1 indicate successor's thread needs unparking 
         if (ws == Node.SIGNAL)
             /*
              * This node has already set status asking a release
              * to signal it, so it can safely park.
              */
             return true;
         if (ws > 0) {
             /*
              * Predecessor was cancelled. Skip over predecessors and
              * indicate retry.
              */
             do {
                 node.prev = pred = pred.prev;
             } while (pred.waitStatus > 0);
             pred.next = node;
         } else {
             /*
              * waitStatus must be 0 or PROPAGATE.  Indicate that we
              * need a signal, but don't park yet.  Caller will need to
              * retry to make sure it cannot acquire before parking.
              */
             compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
         }
         return false;
     }
     
     private final boolean parkAndCheckInterrupt() {
         LockSupport.park(this);
         return Thread.interrupted();
     }
     
     
     ReentrantLock 获取锁流程:
     
     1.若是非公平锁直接尝试加锁(尝试设置同步器状态从0到1,compareAndSetState(0, 1))成功后设置锁持有线程为当前执行线程
       如果失败调用AQS定义的acquire方法 这个方法会调用tryAcquire方法(由ReentrantLock内置同步器自定义实现)再次尝试获
       取锁,加锁失败当前线程以独占模式节点进入等待队列,入队后检查新入队的这个节点的前置节点是否是头节点(队列的头节点是哑节点)
       若是则当前执行线程尝试获取锁,获取锁失败其前置节点waitState设置为-1,循环继续shouldParkAfterFailedAcquire返回true调用
       parkAndCheckInterrupt方法阻塞当前线程节点(操作的过程中跳过失效节点),成功则返回.
     
     2.若是公平锁不会直接加锁,而是尝试加锁,尝试加锁时检查是否有排队的前驱节点,有直接失败,没有再加锁,若失败,加锁失败后的
       逻辑和非公平锁失败后的逻辑一致.
     
     =========================================ReentrantLock(独占模式)释放锁=========================================
     // 掉用同步器的release方法
     public void unlock() {
         sync.release(1);
     } 
     // 即AQS定义的release方法
     public final boolean release(int arg) {
         if (tryRelease(arg)) {
             Node h = head;
             if (h != null && h.waitStatus != 0)
                 unparkSuccessor(h);
             return true;
         }
         return false;
     }    
     // 回调内置同步器的释放锁实现
     protected final boolean tryRelease(int releases) {
         int c = getState() - releases;
         if (Thread.currentThread() != getExclusiveOwnerThread())
             throw new IllegalMonitorStateException();
         boolean free = false;
         if (c == 0) {
             free = true;
             setExclusiveOwnerThread(null);
         }
         // 基本类型的变量(int)的赋值操作是原子的
         setState(c);
         return free;
     }
     // 从后往前唤醒第一个阻塞的节点(waitStatus <= 0)线程
     private void unparkSuccessor(Node node) {
         /*
          * If status is negative (i.e., possibly needing signal) try
          * to clear in anticipation of signalling.  It is OK if this
          * fails or if status is changed by waiting thread.
          */
         int ws = node.waitStatus;
         if (ws < 0)
             compareAndSetWaitStatus(node, ws, 0);
 
         /*
          * Thread to unpark is held in successor, which is normally
          * just the next node.  But if cancelled or apparently null,
          * traverse backwards from tail to find the actual
          * non-cancelled successor.
          */
         Node s = node.next;
         if (s == null || s.waitStatus > 0) {
             s = null;
             for (Node t = tail; t != null && t != node; t = t.prev)
                 if (t.waitStatus <= 0)
                     s = t;
         }
         if (s != null)
             LockSupport.unpark(s.thread);
     }  
     
     
     =========================================ReentrantLock(独占模式)条件队列=========================================
     public class ConditionObject implements Condition, java.io.Serializable {
     
         private static final long serialVersionUID = 1173984872572414699L;
         /** First node of condition queue. */
         private transient Node firstWaiter;
         /** Last node of condition queue. */
         private transient Node lastWaiter;
         ......
     }     
     条件等待
     public final void await() throws InterruptedException {
         if (Thread.interrupted())
             throw new InterruptedException();
         //当前线程加入条件等待队列
         Node node = addConditionWaiter();
         //当前线程释放锁 -- 如果当前线程不持有锁(则无法唤醒在锁上等待的线程) 进而条件等待无意义
         //实际上ReentrantLock的锁实现也会检查当前线程是否持有锁 不持有锁抛出异常
         //因而使用Condition对象做线程协作时必须先持有锁，这和synchronize的关键字是一样的
         
         // 锁释放后会唤醒锁上(同步器实例对象)阻塞的等待线程
         int savedState = fullyRelease(node);
         int interruptMode = 0;
         // 不在同步队列中 阻塞当前线程
         while (!isOnSyncQueue(node)) {
             LockSupport.park(this);
             //被唤醒后转移到同步队列中
             if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                 break;
         }
         // 在同步队列中获取锁或被阻塞
         if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
             interruptMode = REINTERRUPT;
         if (node.nextWaiter != null) // clean up if cancelled
             unlinkCancelledWaiters();
         if (interruptMode != 0)
             reportInterruptAfterWait(interruptMode);
     }
     
     // 线程进入条件等待队列--当前线程持有独占锁因此这是线程安全的
     private Node addConditionWaiter() {
         Node t = lastWaiter;
         // If lastWaiter is cancelled, clean out.
         if (t != null && t.waitStatus != Node.CONDITION) {
             unlinkCancelledWaiters();
             t = lastWaiter;
         }
         Node node = new Node(Thread.currentThread(), Node.CONDITION);
         if (t == null)
             firstWaiter = node;
         else
             t.nextWaiter = node;
         lastWaiter = node;
         return node;
     }
     
     条件通知
     public final void signal() {
         if (!isHeldExclusively())
             throw new IllegalMonitorStateException();
         Node first = firstWaiter;
         if (first != null)
             doSignal(first);
     }
     
     //等待线程收到通知 转移到同步队列
     final boolean transferForSignal(Node node) {
         /*
          * If cannot change waitStatus, the node has been cancelled.
          */
         if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
             return false;
 
         /*
          * Splice onto queue and try to set waitStatus of predecessor to
          * indicate that thread is (probably) waiting. If cancelled or
          * attempt to set waitStatus fails, wake up to resync (in which
          * case the waitStatus can be transiently and harmlessly wrong).
          */
         Node p = enq(node);
         int ws = p.waitStatus;
         if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
             //唤醒阻塞的等待线程
             LockSupport.unpark(node.thread);
         return true;
     }
     =========================================Semaphore(共享模式)获取锁=========================================
          
      public void acquire() throws InterruptedException {
          // AQS定义的共享资源获取
          sync.acquireSharedInterruptibly(1);
      }
      // 尝试获取共享资源
      public final void acquireSharedInterruptibly(int arg)
              throws InterruptedException {
              
          if (Thread.interrupted())
              throw new InterruptedException();
          // 调用Semaphore的tryAcquireShared方法
          if (tryAcquireShared(arg) < 0)
              doAcquireSharedInterruptibly(arg);
      }
      //非公平的
      final int nonfairTryAcquireShared(int acquires) {
          for (;;) {
              int available = getState();
              int remaining = available - acquires;
              if (remaining < 0 ||
                  compareAndSetState(available, remaining))
                  return remaining;
          }
      }
      //公平的
      protected int tryAcquireShared(int acquires) {
          for (;;) {
              //区别于非公平的
              if (hasQueuedPredecessors())
                  return -1;
              int available = getState();
              int remaining = available - acquires;
              if (remaining < 0 ||
                  compareAndSetState(available, remaining))
                  return remaining;
          }
      }
      //获取共享资源失败 -- if (tryAcquireShared(arg) < 0) doAcquireSharedInterruptibly(arg);
      private void doAcquireSharedInterruptibly(int arg)
          throws InterruptedException {
          // 节点为共享模式入队
          final Node node = addWaiter(Node.SHARED);
          boolean failed = true;
          try {
              for (;;) {
                  final Node p = node.predecessor();
                  if (p == head) {
                      int r = tryAcquireShared(arg);
                      if (r >= 0) {
                          //获取资源成功的节点(头节点下一节点-头节点为哑节点)出队并向后唤醒
                          setHeadAndPropagate(node, r);
                          p.next = null; // help GC
                          failed = false;
                          return;
                      }
                  }
                  if (shouldParkAfterFailedAcquire(p, node) &&
                      parkAndCheckInterrupt())
                      throw new InterruptedException();
              }
          } finally {
              if (failed)
                  cancelAcquire(node);
          }
      }
      
      =========================================Semaphore(共享模式)释放锁=========================================
            
      public final boolean releaseShared(int arg) {
          if (tryReleaseShared(arg)) {
              doReleaseShared();
              return true;
          }
          return false;
      }
      
      protected final boolean tryReleaseShared(int releases) {
          for (;;) {
              int current = getState();
              int next = current + releases;
              if (next < current) // overflow
                  throw new Error("Maximum permit count exceeded");
              if (compareAndSetState(current, next))
                  return true;
          }
      }
      
      private void doReleaseShared() {
             
          for (;;) {
              Node h = head;
              if (h != null && h != tail) {
                  int ws = h.waitStatus;
                  if (ws == Node.SIGNAL) {
                      if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                          continue;            // loop to recheck cases
                      unparkSuccessor(h);
                  }
                  else if (ws == 0 &&
                           !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                      continue;                // loop on failed CAS
              }
              if (h == head)                   // loop if head changed
                  break;
          }
      }
      
##Atomic&Unsafe
    Atomic
        Atomic包里一共有12个类，四种原子更新方式，分别是原子更新基本类型，
        原子更新数组，原子更新引用和原子更新字段。
        Atomic包里的类基本都是使用Unsafe实现的包装类。
        
        基本类：  AtomicInteger、AtomicLong、AtomicBoolean；
        引用类型：AtomicReference、AtomicStampedRerence、AtomicMarkableReference； 
        数组类型：AtomicIntegerArray、AtomicLongArray、AtomicReferenceArray 
        属性原子修改器（Updater）：AtomicIntegerFieldUpdater、 AtomicLongFieldUpdater、AtomicReferenceFieldUpdater
    
    Unsafe应用解析
    
        Unsafe是位于sun.misc包下的一个类，主要提供一些用于执行低级别、不安全操作的方法，
        如直接访问系统内存资源、自主管理内存资源等，这些方法在提升Java运行效率、
        增强Java语言底层资源操作能力方面起到了很大的作用。但由于Unsafe类使Java语言拥有了
        类似C语言指针一样操作内存空间的能力，这无疑也增加了程序发生相关指针问题的风险。 
        在程序中过度、不正确使用Unsafe类会使得程序出错的概率变大，使得Java这种安全的语言
        变得不再“安全”，因此对Unsafe的使用一定要慎重。
        
        Unsafe类为一单例实现，提供静态方法getUnsafe获取Unsafe实例，
        当且仅当调用getUnsafe方法的类为引导类加载器所加载时才合法，
        否则抛出SecurityException异常。
        
    Unsafe功能介绍    
        Unsafe提供的API大致可分为:内存操作、CAS、Class相关、对象操作、线程调度、
        系统信息获取、内存屏障、数组操作等几类。
        
        内存操作--这部分主要包含堆外内存的分配、拷贝、释放、给定地址值操作等方法。 
        //分配内存, 相当于C++的malloc函数 
        public native long allocateMemory(long bytes); 
        //扩充内存 
        public native long reallocateMemory(long address, long bytes); 
        //释放内存 
        public native void freeMemory(long address);
        //在给定的内存块中设置值 
        public native void setMemory(Object o, long offset, long bytes, byte value); 
        //内存拷贝 
        public native void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes); 
        //获取给定地址值，忽略修饰限定符的访问限制。与此类似操作还有: getInt， getDouble，getLong，getChar等 
        public native Object getObject(Object o, long offset); 
        //为给定地址设置值，忽略修饰限定符的访问限制，与此类似操作还有: putInt,putDouble，putLong，putChar等 
        public native void putObject(Object o, long offset, Object x); 
        public native byte getByte(long address); 
        //为给定地址设置byte类型的值（当且仅当该内存地址为 allocateMemory分配时，此方法结果才是确定的） 
        public native void putByte(long address, byte x);
        
        通常，Java中创建的对象都处于堆内内存（heap）中，堆内内存是由JVM所管控的Java进程内存，
        并且它们遵循JVM的内存管理机制，JVM会采用垃圾回收机制统一管理堆内存。
        与之相对的是堆外内存，存在于JVM管控之外的内存区域，Java中对堆外内存的操作，
        依赖于Unsafe提供的操作堆外内存的native方法。
        
        使用堆外内存的原因:
            对垃圾回收停顿的改善。由于堆外内存是直接受操作系统管理而不是JVM，
            所以当我们使用堆外内存时，即可保持较小的堆内内存规模。
            从而在GC时减少回收停顿 对于应用的影响。 
        
            提升程序I/O操作的性能。通常在I/O通信过程中，会存在堆内内存到堆外内存的数据拷贝操作，
            对于需要频繁进行内存间数据拷贝且生命周期较短的暂存数据，建议存储到堆外内存。
            
        DirectByteBuffer是Java用于实现堆外内存的一个重要类，通常用在通信过程中做缓冲池，
        如在Netty、MINA等NIO框架中应用广泛。DirectByteBuffer对于堆外内存的创建、 
        使用、销毁等逻辑均由Unsafe提供的堆外内存API来实现。
        
        CAS相关
        
        /*** CAS 
         * @param o 包含要修改field的对象 
         * @param offset 对象中某field的偏移量 
         * @param expected 期望值 
         * @param update 更新值 
         * @return true | false 
         */ 
         public final native boolean compareAndSwapObject(Object o, long offset, Object expected, Object update); 
         public final native boolean compareAndSwapInt(Object o, long offset, int expected, int update); 
         public final native boolean compareAndSwapLong(Object o, long offset, long expected, long update);
         
         线程调度 
         包括线程挂起、恢复、锁机制等方法。 
         //取消阻塞线程 
         public native void unpark(Object thread); 
         //阻塞线程 
         public native void park(boolean isAbsolute, long time); 
         //获得对象锁（可重入锁） 
         @Deprecated public native void monitorEnter(Object o); 
         //释放对象锁 
         @Deprecated public native void monitorExit(Object o); 
         //尝试获取对象锁 
         @Deprecated public native boolean tryMonitorEnter(Object o);
         
         方法park、unpark即可实现线程的挂起与恢复，将一个线程进行挂起是通过park方法实现的，
         调用park方法后，线程将一直阻塞直到超时或者中断等条件出现； 
         unpark可以终止一个挂起的线程，使其恢复正常。
        
         内存屏障 
         Java 8中引入，用于定义内存屏障（也称内存栅栏，内存栅障，屏障指令等，
         是一类 同步屏障指令，是CPU或编译器在对内存随机访问的操作中的一个同步点，
         使得此点之前的 所有读写操作都执行后才可以开始执行此点之后的操作），避免代码重排序。
          
          //内存屏障，禁止load操作重排序。屏障前的load操作不能被重排序到屏 障后，屏障后的load操作不能被重排序到屏障前 
          public native void loadFence(); 
          //内存屏障，禁止store操作重排序。屏障前的store操作不能被重排序到屏障后， 屏障后的store操作不能被重排序到屏障前 
          public native void storeFence(); 
          //内存屏障，禁止load、store操作重排序 
          public native void fullFence();
          
          Java 8中引入了一种锁的新机制——StampedLock，它可以看成是读写锁的一个改进版本。
          StampedLock提供了一种乐观读锁的实现，这种乐观读锁类似于无锁的操作，
          完全不会阻塞写线程获取写锁，从而缓解读多写少时写线程“饥饿”现象。
          由于StampedLock提供的乐观读锁不阻塞写线程获取读锁，
          当线程共享变量从主内存load到线程工作内存时，会存在数据不一致问题，
          所以当使用StampedLock的乐观读锁时，需要遵从如下图用例中使用的模式来确保数据的一致性。
          
          @see /explorations/core-java/concurrent-api/src/main/resources/StampedLock的乐观读锁时遵从的使用模式.PNG
 
 #五、Java并发容器类
 
     BlockingQueue
     
     通常用链表或者数组实现
     一般而言队列具备FIFO先进先出的特性，也有双端队列（Deque）优先级队列
     主要操作：入队（EnQueue）与出队（Dequeue）
     
     ArrayBlockingQueue 由数组支持的有界队列
     LinkedBlockingQueue 由链接节点支持的可选有界队列
     PriorityBlockingQueue 由优先级堆支持的无界优先级队列
     DelayQueue 由优先级堆支持的、基于时间的调度队列


     ConcurrentHashMap
     
     添加元素
     
     final V putVal(K key, V value, boolean onlyIfAbsent) {
         if (key == null || value == null) throw new NullPointerException();
         int hash = spread(key.hashCode());
         int binCount = 0;
         for (Node<K,V>[] tab = table;;) {//自旋
             Node<K,V> f; int n, i, fh;
             if (tab == null || (n = tab.length) == 0)
                 tab = initTable();
             // 插入的K对应的hash位置上还没有元素 使用CAS设置值到对应位置上
             else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                 if (casTabAt(tab, i, null,
                              new Node<K,V>(hash, key, value, null)))
                     break;                   // no lock when adding to empty bin
             }
             // K 对应的hash位置上的节点的hash值是MOVED
             // @see java.util.concurrent.ConcurrentHashMap.transfer
             else if ((fh = f.hash) == MOVED)
                 tab = helpTransfer(tab, f);
             else {
                 V oldVal = null;
                 synchronized (f) {//对桶中第一个Node加锁
                     if (tabAt(tab, i) == f) { //双重检查 取得锁时 可能已经扩容了
                         if (fh >= 0) {
                             binCount = 1;
                             for (Node<K,V> e = f;; ++binCount) {
                                 K ek;
                                 if (e.hash == hash &&
                                     ((ek = e.key) == key ||
                                      (ek != null && key.equals(ek)))) {
                                     oldVal = e.val;
                                     if (!onlyIfAbsent)
                                         e.val = value;
                                     break;
                                 }
                                 Node<K,V> pred = e;
                                 if ((e = e.next) == null) {
                                     pred.next = new Node<K,V>(hash, key,
                                                               value, null);
                                     break;
                                 }
                             }
                         }
                         else if (f instanceof TreeBin) { // 这个桶中的链表节点已转化为树(红黑树)
                             Node<K,V> p;
                             binCount = 2;
                             if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                            value)) != null) {
                                 oldVal = p.val;
                                 if (!onlyIfAbsent)
                                     p.val = value;
                             }
                         }
                     }
                 }
                 if (binCount != 0) {
                     if (binCount >= TREEIFY_THRESHOLD)
                         treeifyBin(tab, i);
                     if (oldVal != null)
                         return oldVal;
                     break;
                 }
             }
         }
         // 扩容
         addCount(1L, binCount);
         return null;
     }

     //hash桶数组初始化
     private final Node<K,V>[] initTable() {
         Node<K,V>[] tab; int sc;
         while ((tab = table) == null || tab.length == 0) {
             if ((sc = sizeCtl) < 0)
                 // 其他线程已在初始 当前线程自旋
                 Thread.yield(); // lost initialization race; just spin
             // 如果当前线程成功取得初始化tab控制权
             else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                 //这个代码块相当于加锁了
                 try {
                     //双重检查 -- 可能有其他线程完已经完成了整个代码块逻辑
                     if ((tab = table) == null || tab.length == 0) {
                         int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                         @SuppressWarnings("unchecked")
                         Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                         table = tab = nt;
                         sc = n - (n >>> 2);
                     }
                 } finally {
                     // 相当于锁释放
                     sizeCtl = sc;
                 }
                 break;
             }
         }
         return tab;
     }
     
     查找元素
     public V get(Object key) {
         Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
         int h = spread(key.hashCode());
         if ((tab = table) != null && (n = tab.length) > 0 &&
             (e = tabAt(tab, (n - 1) & h)) != null) {
             if ((eh = e.hash) == h) {
                 if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                     return e.val;
             }
             else if (eh < 0)
                 return (p = e.find(h, key)) != null ? p.val : null;
             while ((e = e.next) != null) {
                 if (e.hash == h &&
                     ((ek = e.key) == key || (ek != null && key.equals(ek))))
                     return e.val;
             }
         }
         return null;
     }
     
     
     CopyOnWriteArrayList
     
 #六、Java线程池工具  
  
     线程池
      
         “线程池”，就是一个线程缓存，线程是稀缺资源，如果被无限制的创建，不仅会消耗系统资源，
         还会降低系统的稳定性，因此Java中提供线程池对线程进行统一分配、 调优和监控。
          
         线程池为线程生命周期的开销和资源不足问题提供了解决方案。
         通过对多个任务重用线程，线程创建的开销被分摊到了多个任务上。 
          
         什么时候使用线程池？
           
         单个任务处理时间比较短 
         需要处理的任务数量很大 
         (如WEB服务)
          
         线程池优势
          
         重用存在的线程，减少线程创建，消亡的开销。
          
         提高性能提高响应速度，当任务到达时，任务可以不需要的等到线程创建就能立即执行。
           
         提高线程的可管理性，线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，
         还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。
          
     Executor框架
          
     @see /explorations/core-java/concurrent-api/src/main/resources/Executor.png    
      
     Executor子接口ExecutorService，定义了线程池的具体行为
     1，execute(Runnable command)：执行Ruannable类型的任务, 
     2，submit(task)：可用来提交Callable或Runnable任务，并返回代表此任务的Future对象
     3，shutdown()：在完成已提交的任务后封闭办事，不再接管新任务, 
     4，shutdownNow()：停止所有正在执行的任务并封闭办事。 
     5，isTerminated()：测试是否所有任务都履行完毕了。 
     6，isShutdown()：测试是否该ExecutorService已被关闭。
      
     Java线程池工具实现-ThreadPoolExecutor
      
     线程池的创建 
     public ThreadPoolExecutor(int corePoolSize, 
                                  int maximumPoolSize, 
                                  long keepAliveTime, 
                                  TimeUnit unit, 
                                  BlockingQueue<Runnable> workQueue, 
                                  ThreadFactory threadFactory, 
                                  RejectedExecutionHandler handler)
                                  
     corePoolSize 
      
          线程池中的核心线程数，当提交一个任务时，线程池创建一个新线程执行任务，
          直到当前线程数等于corePoolSize；如果当前线程数为corePoolSize，
          继续提交的任务被保存到阻塞队列中等待被执行；如果执行了线程池的prestartAllCoreThreads()方法，
          线程池会提前创建并启动所有核心线程。 
           
     maximumPoolSize 
         线程池中允许的最大线程数。如果当前阻塞队列满了，且继续提交任务，则创建新的线程执行任务，
         前提是当前线程数小于maximumPoolSize；
          
     keepAliveTime 
         线程池维护线程所允许的空闲时间。当线程池中的线程数量大于corePoolSize的时候，
         如果这时没有新的任务提交，核心线程外的线程不会立即销毁，而是会等待，
         直到等待的时间超过了keepAliveTime； 
     unit 
         keepAliveTime的单位； 
          
     workQueue 
      
         用来保存等待被执行的任务的阻塞队列，且任务必须实现Runable接口，
         在JDK中提供了如下阻塞队列： 
         1、ArrayBlockingQueue：基于数组结构的有界阻塞队列，按FIFO排序任务； 
         2、LinkedBlockingQuene：基于链表结构的阻塞队列，按FIFO排序任务，吞吐量通常要高于ArrayBlockingQuene；
         3、SynchronousQuene：一个不存储元素的阻塞队列，每个插入操作必须等到 另一个线程调用移除操作，
            否则插入操作一直处于阻塞状态，吞吐量通常要高于 LinkedBlockingQuene； 
         4、priorityBlockingQuene：具有优先级的无界阻塞队列； 
          
     threadFactory 
      
         它是ThreadFactory类型的变量，用来创建新线程。默认使用 Executors.defaultThreadFactory() 来创建线程。
         使用默认的ThreadFactory来创建线程时，会使新创建的线程具有相同的NORM_PRIORITY优先级并且是非守护线程，同时也设置了线程的名称。 
          
     handler 
      
         线程池的饱和策略，当阻塞队列满了，且没有空闲的工作线程，如果继续提交任务，
         必须采取一种策略处理该任务，线程池提供了4种策略： 
         1、AbortPolicy：直接抛出异常，默认策略； 
         2、CallerRunsPolicy：用调用者所在的线程来执行任务； 
         3、DiscardOldestPolicy：丢弃阻塞队列中靠最前的任务，并执行当前任务； 
         4、DiscardPolicy：直接丢弃任务； 
         上面的4种策略都是ThreadPoolExecutor的内部类。 当然也可以根据应用场景实现RejectedExecutionHandler接口，
         自定义饱和策略，如 记录日志或持久化存储不能处理的任务。
         
     任务提交 
     
     1、public void execute() //提交任务无返回值 
     2、public Future<?> submit() //任务执行完成后有返回值 
     
     线程池监控 
     
     public long getTaskCount() //线程池已执行与未执行的任务总数 
     public long getCompletedTaskCount() //已完成的任务数 
     public int getPoolSize() //线程池当前的线程数 
     public int getActiveCount() //线程池中正在执行任务的线程数量
     
 #七、Java Fork/Join 框架
     
     什么是 Fork/Join 框架
         
         Fork 就是把一个大任务切分为若干子任务并行的执行，
         Join就是合并这些子任务的执行结果，最后得到这个大任务的结果。   
         
     Fork/Jion特性
     
         ForkJoinPool不是为了替代ExecutorService，而是它的补充，
         在某些应用场景 下性能比 ExecutorService 更好。
         
         ForkJoinPool 主要用于实现“分而治之”的算法，特别是分治之后递归调用的函数。
         
         ForkJoinPool 最适合的是计算密集型的任务，如果存在 I/O,线程间同步， sleep() 等
         会造成线程长时间阻塞的情况时，最好配合使用 ManagedBlocker。
          
     工作窃取算法
         
         工作窃取（work-stealing）算法是指某个线程从其他队列里窃取任务来执行。
         
         做一个比较大的任务，可以把这个任务分割为若干互不依赖的子任务，为了减少线程间的竞争，
         于是把这些子任务分别放到不同的队列里，并为每个队列创建一个单独的线程来执行队列里的任务，
         线程和队列一一对应，比如A线程负责处理A队列里的任务。但是有的线程会先把自己队列里的任务干完，
         而其他线程对应的队列里还有任务等待处理。干完活的线程与其等着，不如去帮其他线程干活，
         于是它就去其他线程的队列里窃取一个任务来执行。而在这时它们会访问同一个队列，
         所以为了减少窃取任务线程和被窃取任务线程之间的竞争，通常会使用双端队列，
         被窃取任务线程永远从双端队列的头部拿任务执行，而窃取任务的线程永远从双端队列的尾部拿任务执行。
         
         1. ForkJoinPool的每个工作线程都维护着一个工作队列（WorkQueue），
            这是一 个双端队列（Deque），里面存放的对象是任务（ForkJoinTask）。 
            
         2. 每个工作线程在运行中产生新的任务（通常是因为调用了 fork()）时，会放入工作 队列的队尾，
            并且工作线程在处理自己的工作队列时，使用的是LIFO方式，也就是说每次从队尾取出任务来执行。
            
         3. 每个工作线程在处理自己的工作队列同时，会尝试窃取一个任务（或是来自于刚刚 提交到 pool 的任务，
            或是来自于其他工作线程的工作队列），窃取的任务位于其他 线程的工作队列的队首，
            也就是说工作线程在窃取其他工作线程的任务时，使用的是 FIFO 方式。 
            
         4. 在遇到 join() 时，如果需要 join 的任务尚未完成，则会先处理其他任务，并等待其完成。 
         
         5. 在既没有自己的任务，也没有可以窃取的任务时，进入休眠。
         
     fork/join的使用   
     
         使用 ForkJoin 框架，必须首先创建一个 ForkJoin 任务。它提供在任务中执行 fork() 和 join() 操作的机制，
         通常情况下我们不需要直接继承 ForkJoinTask 类，而只需要继承它的子类，Fork/Join 框架提供了以下两个子类
         
         RecursiveAction：用于没有返回结果的任务。(比如写数据到磁盘，然后就退出了。 一个 RecursiveAction可以把自己的工作分割成更小的几块， 这样它们可以由独立的线程或者CPU执行。 我们可以通过继承来实现一个RecursiveAction) 
         RecursiveTask ：用于有返回结果的任务。(可以将自己的工作分割为若干更小任务，并将这些子任务的执行合并到一个集体结果。 可以有几个水平的分割和合并) 
         
         CountedCompleter： 在任务完成执行后会触发执行一个自定义的钩子函数
         
     fork/join框架原理
     
         @see /explorations/core-java/concurrent-api/src/main/resources/ForkJoin框架执行流程.png
         
         ForkJoinPool构造函数
             
             private ForkJoinPool(int parallelism, 
                                      ForkJoinWorkerThreadFactory factory, 
                                      UncaughtExceptionHandler handler, 
                                      int mode, String workerNamePrefix) { 
             
                 this.workerNamePrefix = workerNamePrefix; 
                 this.factory = factory; this.ueh = handler; 
                 this.config = (parallelism & SMASK) | mode; 
                 long np = (long)(­parallelism); // offset ctl counts 
                 this.ctl = ((np << AC_SHIFT) & AC_MASK) | ((np << TC_SHIFT) & TC_MASK); 
             }
             
             parallelism：并行度（ the parallelism level），默认情况下跟机器的cpu个数保持一致，
             使用 Runtime.getRuntime().availableProcessors()可以得到机器运行时可用的CPU个数。 
             
             factory：创建新线程的工厂（ the factory for creating new threads）。
             默认情况下使 用ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory。
             
             handler：线程异常情况下的处理器（Thread.UncaughtExceptionHandler handler），
             该处理器在线程执行任务时由于某些无法预料到的错误而导致任务线程中断时进行一些处理，默认情况为null。 
             
             asyncMode：这个参数要注意，在ForkJoinPool中，每一个工作线程都有一个独立的任务队列，
             asyncMode表示工作线程内的任务队列是采用何种方式进行调度，可以是先进先出FIFO，
             也可以是后进先出LIFO。如果为true，则线程池中的工作线程则使用先进先出方式进行任务调度，
             默认情况下是false。
             
         fork 方法 
         
         fork() 做的工作只有一件事，既是把任务推入当前工作线程的工作队列里。
         可以参看以下的源代码： 
         public final ForkJoinTask<V> fork() { 
             Thread t; 
             
             if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)
                 ((ForkJoinWorkerThread)t).workQueue.push(this); 
             else
                 ForkJoinPool.common.externalPush(this);
                 
             return this; 
         }
         
         join 方法 
         
         1. 检查调用 join() 的线程是否是 ForkJoinThread 线程。
            如果不是（例如 main 线 程），则阻塞当前线程，等待任务完成。如果是，则不阻塞。
             
         2. 查看任务的完成状态，如果已经完成，直接返回结果。
         
         3. 如果任务尚未完成，但处于自己的工作队列内，则完成它。 
         
         4. 如果任务已经被其他的工作线程偷走，则窃取这个小偷的工作队列内的任务 （以 FIFO 方式）执行，
            以期帮助它早日完成 join 的任务。 
            
         5. 如果偷走任务的小偷也已经把自己的任务全部做完，正在等待需要 join 的任务时，
            则找到小偷的小偷，帮助它完成它的任务。 
            
         6. 递归地执行第5步。
         
         submit 方法 
         
         public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) { 
             if (task == null) throw new NullPointerException(); //提交到工作队列 
             externalPush(task); 
             return task;
         }
         
         ForkJoinPool 自身拥有工作队列，这些工作队列的作用是用来接收由外部线程 （非 ForkJoinThread 线程）提交过来的任务，
         而这些工作队列被称为 submitting queue 。 submit() 和 fork() 其实没有本质区别，
         只是提交对象变成了submitting queue 而已（还有一些同步，初始化的操作）。
         submitting queue 和其他 work queue 一样，是工作线程”窃取“的对象，
         因此当其中的任务被一个工作线程成功窃取时，就意味着提交的任务真正开始进入执行阶段。
         
         
         异常处理
         
         ForkJoinTask 在执行的时候可能会抛出异常，但是我们没办法在主线程里直接捕获异常，
         所以 ForkJoinTask 提供了 isCompletedAbnormally() 方法来检查任务是否已经抛出异常或已经被取消了，
         并且可以通过 ForkJoinTask 的 getException 方法获取异常。例如
         
         if(task.isCompletedAbnormally()){ 
             System.out.println(task.getException()); 
         }
         
         getException 方法返回 Throwable 对象，如果任务被取消了则返回 CancellationException。
         如果任务没有完成或者没有抛出异常则返回 null。
         
===================================================================================
 
##线程安全性

###什么是线程安全性

    当多个线程访问某个类时，不管运行时环境采用何种调度方式或者这些线程如何交替执行
    并且在主调代码中不需要任何额外的同步或协同，类都能表现出正确的行为，那么这个类
    是线程安全的类。
    
    无状态的对象一定是线程安全的
    
###原子性

####竞态条件(Race Condition)

    竞态条件-基于一种可能失效的检测结果来判断执行某个计算
    
    eg.
    
    public class LazyInitRace {
        private ExpensiveObject instance = null
        
        public ExpensiveObject getInstance () {
            // 竞态条件
            if (instance == null) {
                instance = new ExpensiveObject();
            }
            return instance;
        }
    }
    
####复合操作

    包含了一组必须以原子方式执行的操作以确保线程安全性
    
    java.util.concurrent.atomic
    
###加锁机制

    在有多个状态变量的操作中，即使每个状态变量的操作是原子的，
    也无法保证整体的不变性条件不被破坏
    
    在单个原子操作中更新所有相关的状态变量
    
####内置锁   
    //互斥锁
    synchronized(lockObject) {
    }
    
####重入
    如果内置锁不可重入 在子类改写父类的synchronized方法时 会发生死锁。
    
    eg.
    
    public class Widget {
        public synchronized void doSomthing(){
            ...
        }
    }
    
    public class LoggingWidget extends Widget {
        public synchronized void doSomthing(){
            ...
        }
    }
    
###用锁来保护状态

    一种常见的加锁协议是 将所有的可变状态都封装在对象的内部 并通过对象的内置锁对
    所有访问可变状态的代码进行同步，这种加锁协议很容易被破环，新加入方法或代码时
    没有使用同步 这种加锁就会被破坏。
    
    对于包含多个变量的不变性条件 其中所涉及的所有变量都需要由同一个锁来保护
    
    不加区分的滥用synchronized 可能导致程序中出现过多的同步 且只是将每个方法同步
    不足以保证复核操作都是原子的。
    eg.
    if (!vector.contains(element)){
        vector.add(element);
    }
    
###活跃性与性能
    
    活跃性 -- 例如死锁 饥饿 活锁
    
##对象的共享

###可见性

    在没有同步的情况下 编译器 处理器以及运行时等都可能对操作的执行顺序
    进行一些意想不到的调整 在缺乏足够同步的多线程程序中 要想对内存的执
    行顺序进行判断 几乎无法得出正确的结论。
    
    
####失效数据

####非原子的64位操作

####加锁与可见性

    加锁的含义不仅仅局限于互斥行为 还包括内存可见性 为了确保所有的线程
    都能看到共享变量的最新值 所有执行读操作或写操作的线程都必须在同一个
    锁上同步。
    
####volatile变量

    变量被声明为volatile 编译器不会将该变量上的操作与其他内存操作一起重排序
    volatile变量将不会被缓存在寄存器或者对其他处理器不可见的地方。读取volatile
    变量总是返回最新值。
    
    当且仅当满足以下条件时才应该是一volatile变量：
    
    对变量的写入操作不依赖变量的当前值 
    该变量不会与其他状态变量一起纳入不变性条件中
    在访问变量时不需要加锁
    
###发布与逸出

    发布
    
    eg.公共静态变量
    
    public static Set<Secret> knownSecrets;
    
    public void initialize() {
        knownSecrets = new HashSet<>();
    } 
    
    eg.返回私有变量的应用
    
    private String[] states = new String[] {"AK","AL"...}
    public String[] getStates(){return states;}
    
    states 逸出了其所在的作用域
    
    eg.内部的类实例
    
####安全的对象构造过程
    
    使用工厂方法来防止this指针在构造过程中逸出
    
    public class SafeListener {
    
        private final EventListener listener;
        
        private SafeListener() {
            listener = new EventListener () {
                public void onEvent(Event e){
                    ......
                }
            };
        }
        
        public static SafeListener newInstance(EventSource source) {
            SafeListener safe = new SafeListener();
            source.registerListener(safe.listener);
            return safe;
        }
    }
  
  
###线程封闭

####栈封闭 

    局部变量
    并保证局部变量不逸出

####ThreadLocal    

###不变性

    不可变对象一定是线程安全的
    
    不可变对象
    
    对象创建以后其状态就不能修改
    对象的所有域都是final类型
    对象是正确创建的(创建对象时 this指针没有逸出) 
    
####final域

    final类型的域是不能修改的(final域引用的对象是可变的 被引用的对象是可以修改的)
    final域能确保初始化过程中的安全性 从而可以不受限制的访问不可变对象 并在共享这
    些对象时无需同步。
    
###安全发布

####不可变对象与初始化安全性
    
    任何线程都可以在不需要额外同步的情况下安全地访问不可变对象 
    即使在发布这些对象时没有使用同步  
    
    
####安全发布的常用模式

    安全的发布一个对象 对象的引用以及对象的状态必须同时对其他线程可见
    一个正确构造的对象可以通过以下方式来安全的发布
    
    在静态初始化函数中初始化一个对象引用
    将对象的引用保存到volatile类型的域或者AtomicReference对象中
    将对象的引用保存到某个正确构造对象的final域中
    将对象的引用保存到一个由锁保护的域中
    
##对象的组合

###设计线程安全的类

    在设计线程安全的类的过程中需要包含以下三个基本要素
    
    找出构成对象状态的所有变量
    找出约束变量状态的不变性条件
    建立对象状态的并发访问管理策略
    
###实例封闭

    将数据封装在对象内部 可以将数据的访问限制在对象的方法上 从而更容易确保
    线程在访问数据时总能持有正确的锁
    
    Java提供的容器包装类就是一种实例封闭 如 Collections.synchronizedList
    
###线程安全的委托

    将线程的安全性委托给现有的线程安全的类
    
###在现有的线程安全类中添加功能  - 最好的方式是使用组合
    
    public class ImprovedList<T> implements List<T> {
        //确保类拥有指向底层List的唯一外部引用 确保线程安全
        private final List<T> = new List<T>();
        
        public synchronized boolean putIfAbsent(T x) {
            boolen contains = list.contains(x);
            if (!contains) {
                list.add(x);
            }
            return contains;
        }
    }
    
    
##基础模块构建

###同步容器类

    复合操作的安全性
    
    迭代(同步容器类的迭代并没有考虑并发修改 即如果不对整个的迭代过程加锁就可能出现ConcurrentModificationException)
    与ConcurrentModificationException 
    
    如果不希望在迭代期间对容器加锁 可以克隆(克隆时加锁)容器 
    将其克隆副本封闭在线程内
    
    
###并发容器

    
###阻塞队列和生产者-消费者模式


##任务执行

###在线程中执行任务

####串行的执行任务

    public class SingleThreadWebServer {
        public static void main(String[] args) throwsIOException {
            ServerSocket socket = new ServerSocket(80);
            while (true) {
                Socket connection = socket.accept();
                handleRequest(connection);
            }
        }
    }

####显示的为任务创建线程

    public class ThreadPerTaskWebServer {
        public static void main(String[] args) throwsIOException {
            ServerSocket socket = new ServerSocket(80);
            while (true) {
                final Socket connection = socket.accept();
                Runnable task = new Runnable(){
                    public void run() {
                        handleRequest(connection);
                    }
                };
                
                new Thread(task).start();
            }
        }
    }

####无限制的创建线程的不足

    线程的生命周期开销非常高
    资源消耗
    稳定性
    
###Executor框架

    public class TaskExecutionWebserver {
        private static final int NTHREADS = 100;
        private static final Executor exec = 
            Executors.newFixedThreadPool(NTHREADS);
        
        public static void main(String[] args) throwsIOException {
            ServerSocket socket = new ServerSocket(80);
            while (true) {
                final Socket connection = socket.accept();
                Runnable task = new Runnable(){
                    public void run() {
                        handleRequest(connection);
                    }
                };
                
                exec.execute(task);
            }
        }
    }
    
####线程池

###找出可利用的并行性


##显示锁

###Lock与ReentrantLock

####轮询锁与定时锁
####可中断的锁获取操作

###公平性

###在synchronized和ReentrantLock之间选择

###读-写锁    
