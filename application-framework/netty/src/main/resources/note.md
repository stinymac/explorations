#NIO与零拷贝
    
     Java中，常用的零拷贝有 mmap(内存映射) 和 sendFile。
     零拷贝，是从操作系统的角度来说的。因为内核缓冲区之间，
     没有数据是重复的（只有 kernel buffer 有一份数据）。
     零拷贝不仅仅带来更少的数据复制，还能带来其他的性能优势，
     如更少的上下文切换，更少的 CPU 缓存伪共享以及无 CPU 校验和计算。
     
     传统IO数据拷贝即状态切换
     @see /explorations/application-framework/netty/src/main/resources/imgs/generalIO.png
     
     mmap数据拷贝即状态切换
     @see /explorations/application-framework/netty/src/main/resources/imgs/mmap.png
     
     mmap 通过内存映射，将文件映射到内核缓冲区，同时，用户空间可以共享内核空间的数据。
     这样，在进行网络传输时，就可以减少内核空间到用户控件的拷贝次数。
     
     sendFile(linux 2.1)数据拷贝即状态切换   
     @see /explorations/application-framework/netty/src/main/resources/imgs/sendfile.png
   
     Linux 2.1 版本 提供的sendFile 函数:数据不经过用户态，直接从内核缓冲区进入到 Socket Buffer，
     同时，由于和用户态完全无关，就减少了一次上下文切换

       
     zero copy数据拷贝即状态切换
     @see /explorations/application-framework/netty/src/main/resources/imgs/sendfile(zero-copy).png
     Linux 在 2.4 版本中提供的sendFile 函数:
     避免了从内核缓冲区拷贝到Socket buffer的操作(拷贝的信息很少，比如lenght , offset , 消耗低，可以忽略)，
     直接拷贝到协议栈，从而再一次减少了数据拷贝。  
     
     mmap 和 sendFile
     
     mmap 适合小数据量读写，sendFile 适合大文件传输。
     
     mmap 需要 4 次上下文切换，3 次数据拷贝；sendFile 需要 3 次上下文切换，最少 2 次数据拷贝。
     
     sendFile 可以利用 DMA 方式，减少 CPU 拷贝，mmap 则不能（必须从内核拷贝到 Socket 缓冲区）   
          
     
#Netty概述 

    原生NIO存在的问题
        
        断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流的处理等等。
        Epoll Bug，它会导致 Selector 空轮询，最终导致 CPU 100%。
        
    Netty 对 JDK 自带的 NIO 的 API 进行了封装，解决了上述问题。
    
#Netty高性能架构设计    

    Reactor模型
    
    Reactor：Reactor 在一个单独的线程中运行，负责监听和分发事件，分发给适当的处理程序来对 IO 事件做出反应。
             它就像电话接线员，它接听来自客户的电话并将线路转移到适当的联系人；
    
    Handlers：处理程序执行 I/O 事件要完成的实际事件。
              Reactor 通过调度适当的处理程序来响应 I/O 事件，处理程序执行非阻塞操作。
              
    
    Reactor模型 3种实现
    
    单Reactor单线程
    
        Select 是前面 I/O 复用模型介绍的标准网络编程 API，可以实现应用程序通过一个阻塞对象监听多路连接请求
       
        Reactor 对象通过 Select 监控客户端请求事件，收到事件后通过 Dispatch 进行分发
        
        如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接完成后的后续业务处理
        
        如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应
       
        Handler 会完成 Read→业务处理→Send 的完整业务流程
        
        单Reactor单线程分析
        
        优点：模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成
        缺点：性能问题，只有一个线程，无法完全发挥多核 CPU 的性能。
              Handler 在处理某个连接上的业务时，整个进程无法处理其他连接事件，很容易导致性能瓶颈
        缺点：可靠性问题，线程意外终止，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，
             造成节点故障

        单Reactor多线程
        
        Reactor 对象通过select 监控客户端请求事件, 收到事件后，通过dispatch进行分发
        
        如果建立连接请求, 则右Acceptor 通过accept 处理连接请求, 然后创建一个Handler对象处理完成连接后的各种事件
        
        如果不是连接请求，则由reactor分发调用连接对应的handler 来处理
        
        handler 只负责响应事件，不做具体的业务处理, 通过read 读取数据后，会分发给后面的worker线程池的某个线程处理业务
       
        worker 线程池会分配独立线程完成真正的业务，并将结果返回给handler
        
        handler收到响应后，通过send 将结果返回给client
        
        单Reactor多线程分析
        
        优点：可以充分的利用多核cpu 的处理能力
        缺点：多线程数据共享和访问比较复杂， reactor 处理所有的事件的监听和响应，
              在单线程运行， 在高并发场景容易出现性能瓶颈.

    主从Reactor多线程
    
        Reactor主线程 MainReactor 对象通过select 监听连接事件, 收到事件后，通过Acceptor 处理连接事件
        
        当 Acceptor  处理连接事件后，MainReactor 将连接分配给SubReactor 
        
        subreactor 将连接加入到连接队列进行监听,并创建handler进行各种事件处理
        
        当有新事件发生时， subreactor 就会调用对应的handler处理
       
        handler 通过read 读取数据，分发给后面的worker 线程处理
        
        worker 线程池分配独立的worker 线程进行业务处理，并返回结果
        
        handler 收到响应的结果后，再通过send 将结果返回给client
       
        Reactor 主线程可以对应多个Reactor 子线程, 即MainRecator 可以关联多个SubReactor
        
    主从Reactor多线程分析
    
        优点：父线程与子线程的数据交互简单职责明确，父线程只需要接收新连接，
             子线程完成后续的业务处理。
             
        优点：父线程与子线程的数据交互简单，Reactor 主线程只需要把新连接传给子线程，
             子线程无需返回数据。
             
        缺点：编程复杂度较高    
        
    Reactor 模式具有如下的优点
    
    响应快，不必为单个同步时间所阻塞，虽然 Reactor 本身依然是同步的
    可以最大程度的避免复杂的多线程及同步问题，并且避免了多线程/进程的切换开销
    扩展性好，可以方便的通过增加 Reactor 实例个数来充分利用 CPU 资源
    复用性好，Reactor 模型本身与具体事件处理逻辑无关，具有很高的复用性
    
    Netty模型
    
    Netty 主要基于主从 Reactors 多线程模型做了一定的改进，其中主从 Reactor 多线程模型有多个 Reactor
    
    @see /explorations/application-framework/netty/src/main/resources/imgs/netty-io-model.png
    
    Netty抽象出两组线程池 BossGroup 专门负责接收客户端的连接, WorkerGroup 专门负责网络的读写
    
    BossGroup 和 WorkerGroup 类型都是 NioEventLoopGroup
    
    NioEventLoopGroup 相当于一个事件循环组, 这个组中含有多个事件循环 ，每一个事件循环是 NioEventLoop
    
    NioEventLoop 表示一个不断循环的执行处理任务的线程， 每个NioEventLoop 都有一个selector , 用于监听绑定在其上的socket的网络通讯
   
    NioEventLoopGroup 可以有多个线程, 即可以含有多个NioEventLoop
    
    每个Boss NioEventLoop 循环执行的步骤有3步
        轮询accept 事件
        处理accept 事件 , 与client建立连接 , 生成NioScocketChannel , 并将其注册到某个worker NIOEventLoop 上的 selector 
        处理任务队列的任务 ， 即 runAllTasks
        
    每个 Worker NIOEventLoop 循环执行的步骤
        轮询read, write 事件
        处理i/o事件， 即read , write 事件，在对应NioScocketChannel 处理
        处理任务队列的任务 ， 即 runAllTasks
        
    每个Worker NIOEventLoop  处理业务时，会使用pipeline(管道), pipeline 中包含了 channel , 即通过pipeline 可以获取到对应通道, 管道中维护了很多的 处理器

