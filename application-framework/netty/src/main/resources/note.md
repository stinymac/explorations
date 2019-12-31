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
        处理I/O事件， 即read , write 事件，在对应NioScocketChannel 处理
        处理任务队列的任务 ， 即 runAllTasks
        
    每个Worker NIOEventLoop  处理业务时，会使用pipeline(管道), pipeline 中包含了 channel , 即通过pipeline 可以获取到对应通道, 管道中维护了很多的 处理器

    异步模型
    
    Netty 中的 I/O 操作是异步的，包括 Bind、Write、Connect 等操作会简单的返回一个 ChannelFuture。
    调用者并不能立刻获得结果，而是通过 Future-Listener 机制，用户可以方便的主动获取或者通过通知机制获得 IO 操作结果
    Netty 的异步模型是建立在 future 和 callback 的之上的。它的核心思想是：
    假设一个方法 fun，计算过程可能非常耗时，等待 fun返回显然不合适。那么可以在调用 fun 的时候，
    立马返回一个 Future，后续可以通过 Future去监控方法 fun 的处理过程(即 ： Future-Listener 机制)

#Netty核心模块组件   

    Bootstrap、ServerBootstrap
    
    Bootstrap 意思是引导，一个 Netty 应用通常由一个 Bootstrap 开始，
    主要作用是配置整个 Netty 程序，串联各个组件，Netty 中 Bootstrap 
    类是客户端程序的启动引导类，ServerBootstrap 是服务端启动引导类。
    
    public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)，该方法用于服务器端，用来设置两个EventLoop
    public B group(EventLoopGroup group) ，该方法用于客户端，用来设置一个 EventLoop
    public B channel(Class<? extends C> channelClass)，该方法用来设置一个服务器端的通道实现
    public <T> B option(ChannelOption<T> option, T value)，用来给 ServerChannel 添加配置
    public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value)，用来给接收到的通道添加配置
    public ServerBootstrap childHandler(ChannelHandler childHandler)，该方法用来设置业务处理类（自定义的 handler）
    public ChannelFuture bind(int inetPort) ，该方法用于服务器端，用来设置占用的端口号
    public ChannelFuture connect(String inetHost, int inetPort) ，该方法用于客户端，用来连接服务器端

    Future、ChannelFuture
    
    Netty 中所有的 IO 操作都是异步的，不能立刻得知消息是否被正确处理。
    但是可以等它执行完成或者直接注册一个监听，具体的实现就是通过 Future 
    和 ChannelFutures，注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件。
    
    Channel channel()，返回当前正在进行 IO 操作的通道
    ChannelFuture sync()，等待异步操作执行完毕
    
    Channel
    
    Netty 网络通信的组件，能够用于执行网络 I/O 操作。
    
    通过Channel 可获得当前网络连接的通道的状态，网络连接的配置参数 （例如接收缓冲区大小）。
    
    Channel 提供异步的网络 I/O 操作(如建立连接，读写，绑定端口)，
    异步调用意味着任何 I/O 调用都将立即返回，并且不保证在调用结束时所请求的 I/O 操作已完成。
    
    调用立即返回一个 ChannelFuture 实例，通过注册监听器到 ChannelFuture 上，可以在I/O 操作成功、失败或取消时回调通知调用方。
    
    支持关联 I/O 操作与对应的处理程序
    
    不同协议、不同的阻塞类型的连接都有不同的 Channel 类型与之对应，常用的 Channel 类型:
    
    NioSocketChannel，异步的客户端 TCP Socket 连接。
    NioServerSocketChannel，异步的服务器端 TCP Socket 连接。
    NioDatagramChannel，异步的 UDP 连接。
    NioSctpChannel，异步的客户端 Sctp 连接。
    NioSctpServerChannel，异步的 Sctp 服务器端连接，这些通道涵盖了UDP和TCP网络IO以及文件 IO。
    
    Selector
    
    Netty 基于 Selector 对象实现 I/O 多路复用，通过 Selector 一个线程可以监听多个连接的 Channel 事件。
    
    当向一个 Selector 中注册 Channel 后，Selector 内部的机制就可以自动不断地查询(Select) 这些注册的 Channel 
    是否有已就绪的 I/O 事件（例如可读，可写，网络连接完成等），程序就可以很简单地使用一个线程高效地管理多个 Channel 。

    ChannelHandler 及其实现类
    
    ChannelHandler 是一个接口，处理 I/O 事件或拦截 I/O 操作，并将其转发到其 ChannelPipeline(业务处理链)中的下一个处理程序。
   
    ChannelInboundHandler 用于处理入站 I/O 事件。
    ChannelOutboundHandler 用于处理出站 I/O 操作。
    
    ChannelInboundHandlerAdapter 用于处理入站 I/O 事件。
    ChannelOutboundHandlerAdapter 用于处理出站 I/O 操作。
    ChannelDuplexHandler 用于处理入站和出站事件。
    
    Pipeline 和 ChannelPipeline
    
    ChannelPipeline 是一个 Handler 的集合，它负责处理和拦截 inbound 或者 outbound 的事件和操作，
    相当于一个贯穿 Netty 的链。(可以这样理解：ChannelPipeline 是 保存 ChannelHandler 的 List，用于处理或拦截 Channel 的入站事件和出站操作)
    
    ChannelPipeline 实现了一种高级形式的拦截过滤器模式，使用户可以完全控制事件的处理方式，以及 Channel 中各个的 ChannelHandler 如何相互交互
    
    在 Netty 中每个 Channel 都有且仅有一个 ChannelPipeline 与之对应
    
    一个 Channel 包含了一个 ChannelPipeline，而 ChannelPipeline 中又维护了一个由 ChannelHandlerContext 组成的双向链表，
    并且每个 ChannelHandlerContext 中又关联着一个 ChannelHandler
    
    入站事件和出站事件在一个双向链表中，入站事件会从链表 head 往后传递到最后一个入站的 handler，
    出站事件会从链表 tail 往前传递到最前一个出站的 handler，两种类型的 handler 互不干扰。
    
    ChannelHandlerContext
    
    保存 Channel 相关的所有上下文信息，同时关联一个 ChannelHandler 对象
    即ChannelHandlerContext 中包含一个具体的事件处理器 ChannelHandler ， 
    同 时ChannelHandlerContext 中也绑定了对应的 pipeline 和 Channel 的信息，
    方便对 ChannelHandler进行调用.
    
    ChannelOption
    
    Netty 在创建 Channel 实例后,一般都需要设置 ChannelOption 参数。
    
    ChannelOption.SO_BACKLOG
    对应 TCP/IP 协议 listen 函数中的 backlog 参数，用来初始化服务器可连接队列大小。服
    务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接。多个客户
    端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog 参数指定
    了队列的大小。
    
    ChannelOption.SO_KEEPALIVE
    一直保持连接活动状态
    
    EventLoopGroup 和其实现类 NioEventLoopGroup
    
    EventLoopGroup 是一组 EventLoop 的抽象，Netty 为了更好的利用多核 CPU 资源，
    一般会有多个 EventLoop 同时工作，每个 EventLoop 维护着一个 Selector 实例。
    
    EventLoopGroup 提供 next 接口，可以从组里面按照一定规则获取其中一个 EventLoop来处理任务。
    在 Netty 服务器端编程中，一般都需要提供两个 EventLoopGroup，例如：BossEventLoopGroup 和 WorkerEventLoopGroup。
    
    通常一个服务端口即一个 ServerSocketChannel对应一个Selector 和一个EventLoop线程。
    BossEventLoop 负责接收客户端的连接并将 SocketChannel 交给 WorkerEventLoopGroup 来进行 IO 处理。
    
    Unpooled 类
    
    Netty 提供一个专门用来操作缓冲区(即Netty的数据容器)的工具类
    
#Netty编解码器
    

    

    
