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

package org.mac.explorations.framework.netty.intro;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;

/**
 * Netty的启动流程
 *
 *     两个事件循环组(主从Reactor)的创建
 *     @see NioEventLoopGroup#NioEventLoopGroup(int, java.util.concurrent.Executor, java.nio.channels.spi.SelectorProvider, io.netty.channel.SelectStrategyFactory)
 *     @see MultithreadEventExecutorGroup#MultithreadEventExecutorGroup(int, java.util.concurrent.Executor, io.netty.util.concurrent.EventExecutorChooserFactory, java.lang.Object...)
 *     int nThreads,                                线程数量(不指定则是 CPU核数 * 2)
 *     Executor executor                            执行线程池(默认创建的线程是FastThreadLocalThread) @see {@link io.netty.util.concurrent.FastThreadLocalThread}
 *     EventExecutorChooserFactory chooserFactory   @see {@link io.netty.util.concurrent.DefaultEventExecutorChooserFactory}
 *     SelectorProvider selectorProvider            选择器加载
 *     SelectStrategyFactory selectStrategyFactory  @see {@link io.netty.channel.DefaultSelectStrategy,io.netty.channel.SelectStrategy}
 *     RejectedExecutionHandler reject              线程池拒绝处理器
 *
 *     1. 执行器Executor为空创建默认的执行器 @see {@link io.netty.util.concurrent.ThreadPerTaskExecutor,io.netty.util.concurrent.FastThreadLocalThread}
 *     // 即为该执行器的每次执行创建一个线程
 *     public void execute(Runnable command) {
 *         threadFactory.newThread(command).start();
 *     }
 *     2. 创建子事件循环执行器数组(相当于线程池)并初始化
 *
 *     children = new EventExecutor[nThreads];
 *
 *     for (int i = 0; i < nThreads; i ++) {
 *         boolean success = false;
 *         try {
 *             @see NioEventLoopGroup#newChild(java.util.concurrent.Executor, java.lang.Object...)
 *             children[i] = newChild(executor, args);
 *             success = true;
 *         } catch (Exception e) {
 *             // TODO: Think about if this is a good exception type
 *             throw new IllegalStateException("failed to create a child event loop", e);
 *         } finally {
 *           ....
 *         }
 *     }
 *
*    子事件循环执行器默认为NioEventLoop
 *   @see io.netty.channel.nio.NioEventLoop
 *   @see io.netty.channel.SingleThreadEventLoop
 *   @see io.netty.util.concurrent.SingleThreadEventExecutor
 *
 *  子事件循环执行器初始化
 *
 *  @see io.netty.channel.nio.NioEventLoopGroup#newChild(java.util.concurrent.Executor, java.lang.Object...)
 *  @see io.netty.channel.nio.NioEventLoop#NioEventLoop(io.netty.channel.nio.NioEventLoopGroup, java.util.concurrent.Executor, java.nio.channels.spi.SelectorProvider, io.netty.channel.SelectStrategy, io.netty.util.concurrent.RejectedExecutionHandler, io.netty.channel.EventLoopTaskQueueFactory)
 *  @see io.netty.channel.SingleThreadEventLoop#SingleThreadEventLoop(io.netty.channel.EventLoopGroup, java.util.concurrent.Executor, boolean, java.util.Queue, java.util.Queue, io.netty.util.concurrent.RejectedExecutionHandler)
 *  @see io.netty.util.concurrent.SingleThreadEventExecutor#SingleThreadEventExecutor(io.netty.util.concurrent.EventExecutorGroup, java.util.concurrent.Executor, boolean, java.util.Queue, io.netty.util.concurrent.RejectedExecutionHandler)
 *
 *  3.初始化事件执行器选择器(选择器在执行器中(线程池)循环选择线程)
 *  @see io.netty.util.concurrent.DefaultEventExecutorChooserFactory#newChooser(io.netty.util.concurrent.EventExecutor[])
 *  @see io.netty.util.concurrent.DefaultEventExecutorChooserFactory.PowerOfTwoEventExecutorChooser
 *  @see io.netty.util.concurrent.DefaultEventExecutorChooserFactory.GenericEventExecutorChooser
 *
 *  4.为每个执行器添加默认的停止监听器
 *
 *  5.初始化只读的子事件循环执行器
 *  readonlyChildren = Collections.unmodifiableSet(childrenSet);
 *
 *  启动引导类创建
 *
 *  @see io.netty.bootstrap.ServerBootstrap
 *
 *      @see io.netty.bootstrap.ServerBootstrap#childOptions
 *      @see io.netty.bootstrap.ServerBootstrap#childAttrs
 *      @see io.netty.bootstrap.ServerBootstrap#childGroup
 *      @see io.netty.bootstrap.ServerBootstrap#childHandler
 *
 *  @see io.netty.bootstrap.AbstractBootstrap
 *      @see io.netty.bootstrap.AbstractBootstrap#group
 *      @see io.netty.bootstrap.AbstractBootstrap#channelFactory
 *      @see io.netty.bootstrap.AbstractBootstrap#localAddress
 *      @see io.netty.bootstrap.AbstractBootstrap#options
 *      @see io.netty.bootstrap.AbstractBootstrap#attrs
 *      @see io.netty.bootstrap.AbstractBootstrap#handler
 *
 *  启动引导类配置
 *     将两个事件执行循环组分别赋值给group和childGroup
 *     @see io.netty.bootstrap.ServerBootstrap#group(io.netty.channel.EventLoopGroup, io.netty.channel.EventLoopGroup)
 *     @see io.netty.bootstrap.AbstractBootstrap#group
 *
 *     option()选项配置参数放置到io.netty.bootstrap.AbstractBootstrap#options中(ConcurrentHashMap)
 *     同样的childOption()选项配置参数放置到io.netty.bootstrap.ServerBootstrap#childOptions中
 *
 *     handler的设置同option一样ChannelHandler handler是主事件循环组的处理器,ChannelHandler  childHandler是工作事件循环组的的处理器
 *
 *     配置channel
 *     @see io.netty.bootstrap.AbstractBootstrap#channel(java.lang.Class)
 *     即将channelFactory 初始化为  @see {@link io.netty.channel.ReflectiveChannelFactory}
 *
 *     启动服务
 *     // bootstrap.bind(6668).sync();
 *     @see io.netty.bootstrap.AbstractBootstrap#doBind(java.net.SocketAddress)
 *
 *     初始化channel和注册
 *     @see io.netty.bootstrap.AbstractBootstrap#initAndRegister()
 *
 *     channel = channelFactory.newChannel();
 *     创建channel 即指定的 @see {@link io.netty.channel.socket.nio.NioServerSocketChannel}
 *     @see io.netty.channel.socket.nio.NioServerSocketChannel#NioServerSocketChannel(java.nio.channels.ServerSocketChannel)
 *     @see io.netty.channel.AbstractChannel#AbstractChannel(io.netty.channel.Channel)
 *
 *     主要创建了NioServerSocketChannel 对应的Java的ServerSocketChannel pipeline 指定SelectionKey.OP_ACCEPT 等
 *
 *     初始化channel
 *     @see io.netty.bootstrap.ServerBootstrap#init(io.netty.channel.Channel)
 *
 *         对channel进行初始配置
 *
 *         对channel关联的pipeline进行初始配置
 *         <pre>
 *              p.addLast(new ChannelInitializer<Channel>() {
 *                  @Override
 *                  public void initChannel(final Channel ch) {
 *                      final ChannelPipeline pipeline = ch.pipeline();
 *                      ChannelHandler handler = config.handler();
 *                      // bossGroup的handler
 *                      if (handler != null) {
 *                          pipeline.addLast(handler);
 *                      }
 *                      // 添加bossGroup关联的Child以ServerBootstrapAcceptor类型的handler
 *                      ch.eventLoop().execute(new Runnable() {
 *                          @Override
 *                          public void run() {
 *                              pipeline.addLast(new ServerBootstrapAcceptor(ch, currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs));
 *                          }
 *                     });
 *                  }
 *              });
 *         </pre>
 *
 *         将初始化后的channel注册到EventGLoopGroup
 *
 *            @see io.netty.channel.SingleThreadEventLoop#register(io.netty.channel.Channel)
 *            @see io.netty.channel.SingleThreadEventLoop#register(io.netty.channel.ChannelPromise)
 *            @see io.netty.channel.AbstractChannel.AbstractUnsafe#register0(io.netty.channel.ChannelPromise)
 *
 *                Java NIO的ServerSocketChannel上注册selector 监听ACCEPT事件
 *                @see io.netty.channel.nio.AbstractNioChannel#doRegister()
 *
 *           @see io.netty.channel.AbstractChannel.AbstractUnsafe#bind(java.net.SocketAddress, io.netty.channel.ChannelPromise)
 *           @see io.netty.channel.socket.nio.NioServerSocketChannel#doBind(java.net.SocketAddress)
 *
 *        注册后开始监听连接事件
 *        @see io.netty.channel.nio.NioEventLoop#run()
 *
 * Netty的请求流程
 *
 *    Server启动后 监听并选择事件 客户端发起连接 bossEventLoop选择到事件后 处理选择的事件
 *    @see io.netty.channel.nio.NioEventLoop#processSelectedKey(java.nio.channels.SelectionKey, io.netty.channel.nio.AbstractNioChannel)
 *    @see io.netty.channel.nio.AbstractNioMessageChannel.NioMessageUnsafe#read()
 *    @see io.netty.channel.socket.nio.NioServerSocketChannel#doReadMessages(java.util.List)
 *
 *    //完成客户端连接
 *    SocketChannel ch = SocketUtils.accept(javaChannel());
 *
 *    if (ch != null) {
 *        // 将创建的连接包装为NioSocketChannel
 *        buf.add(new NioSocketChannel(this, ch));
 *        return 1;
 *    }
 *
 *   完成客户端连接后调用pipline上注册的处理器 其中ServerBootstrapAcceptor处理分发
 *
 *   @see io.netty.bootstrap.ServerBootstrap.ServerBootstrapAcceptor#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
 *
 *   // 新获取的连接
 *   final Channel child = (Channel) msg;
 *   // 设置childHandler
 *   child.pipeline().addLast(childHandler);
 *
 *   setChannelOptions(child, childOptions, logger);
 *   setAttributes(child, childAttrs);
 *
 *   try {
 *       // 将新连接(NioSocketChannel)注册到(默认从workerGroup循环选取EventLoop对象)childGroup即workerGroup
 *       childGroup.register(child).addListener(new ChannelFutureListener() {
 *           @Override
 *           public void operationComplete(ChannelFuture future) throws Exception {
 *               if (!future.isSuccess()) {
 *                  forceClose(child, future.cause());
 *               }
 *           }
 *       });
 *   } catch (Throwable t) {
 *       forceClose(child, t);
 *   }
 *
 * Netty的IO处理核心组件ChannelPipeline ChannelHandlerContext ChannelHandler
 *
 * ChannelPipeline(内部维护一个双向链表)
 *
 * public interface ChannelPipeline
 * extends ChannelInboundInvoker, ChannelOutboundInvoker, Iterable<Entry<String, ChannelHandler>>
 * <pre>
 *                                                 I/O Request
 *                                            via {@link Channel} or
 *                                        {@link ChannelHandlerContext}
 *                                                      |
 *  +---------------------------------------------------+---------------+
 *  |                           ChannelPipeline         |               |
 *  |                                                  \|/              |
 *  |    +---------------------+            +-----------+----------+    |
 *  |    | Inbound Handler  N  |            | Outbound Handler  1  |    |
 *  |    +----------+----------+            +-----------+----------+    |
 *  |              /|\                                  |               |
 *  |               |                                  \|/              |
 *  |    +----------+----------+            +-----------+----------+    |
 *  |    | Inbound Handler N-1 |            | Outbound Handler  2  |    |
 *  |    +----------+----------+            +-----------+----------+    |
 *  |              /|\                                  .               |
 *  |               .                                   .               |
 *  | ChannelHandlerContext.fireIN_EVT() ChannelHandlerContext.OUT_EVT()|
 *  |        [ method call]                       [method call]         |
 *  |               .                                   .               |
 *  |               .                                  \|/              |
 *  |    +----------+----------+            +-----------+----------+    |
 *  |    | Inbound Handler  2  |            | Outbound Handler M-1 |    |
 *  |    +----------+----------+            +-----------+----------+    |
 *  |              /|\                                  |               |
 *  |               |                                  \|/              |
 *  |    +----------+----------+            +-----------+----------+    |
 *  |    | Inbound Handler  1  |            | Outbound Handler  M  |    |
 *  |    +----------+----------+            +-----------+----------+    |
 *  |              /|\                                  |               |
 *  +---------------+-----------------------------------+---------------+
 *                  |                                  \|/
 *  +---------------+-----------------------------------+---------------+
 *  |               |                                   |               |
 *  |       [ Socket.read() ]                    [ Socket.write() ]     |
 *  |                                                                   |
 *  |  Netty Internal I/O Threads (Transport Implementation)            |
 *  +-------------------------------------------------------------------+
 * </pre>
 *
 * @see io.netty.channel.ChannelPipeline
 *
 * 创建Netty的channel(new NioSocketChannel(this, ch))包装Java的channel的时候会同时创建
 * @see io.netty.channel.nio.AbstractNioByteChannel#AbstractNioByteChannel(io.netty.channel.Channel, java.nio.channels.SelectableChannel)
 * @see io.netty.channel.nio.AbstractNioChannel#AbstractNioChannel(io.netty.channel.Channel, java.nio.channels.SelectableChannel, int)
 * @see io.netty.channel.AbstractChannel#AbstractChannel(io.netty.channel.Channel)
 * @see io.netty.channel.DefaultChannelPipeline#DefaultChannelPipeline(io.netty.channel.Channel)
 * <pre>
 *     //pipeline引用channel
 *     this.channel = ObjectUtil.checkNotNull(channel, "channel");
 *     //链表初始化
 *     tail = new TailContext(this);
 *     head = new HeadContext(this);
 *
 *     head.next = tail;
 *     tail.prev = head;
 * </pre>
 *
 * 向pipeline中添加处理器(使用DefaultChannelContext包装handler @see {@link io.netty.channel.DefaultChannelPipeline#newContext(io.netty.util.concurrent.EventExecutorGroup, java.lang.String, io.netty.channel.ChannelHandler)})
 * @see io.netty.channel.DefaultChannelPipeline#addLast(io.netty.util.concurrent.EventExecutorGroup, java.lang.String, io.netty.channel.ChannelHandler)
 *
 * 查找处理器执行任务
 * channel上的读事件就绪 EventLoop 选择到Key开始处理读任务
 * @see io.netty.channel.nio.AbstractNioByteChannel.NioByteUnsafe#read()
 *
 * 完成读后调用 pipeline.fireChannelRead(byteBuf);
 * @see io.netty.channel.DefaultChannelPipeline#fireChannelRead(java.lang.Object)
 * //head--pipeline维护的链表头 msg--读到的字节
 * AbstractChannelHandlerContext.invokeChannelRead(head, msg);
 * @see io.netty.channel.AbstractChannelHandlerContext#invokeChannelRead(io.netty.channel.AbstractChannelHandlerContext, java.lang.Object)

 * @see io.netty.channel.AbstractChannelHandlerContext#invokeChannelRead(io.netty.channel.AbstractChannelHandlerContext, java.lang.Object)
 * @see io.netty.channel.AbstractChannelHandlerContext#invokeChannelRead(java.lang.Object)
 *
 * 处理后查找下一个处理器
 * @see io.netty.channel.AbstractChannelHandlerContext#findContextInbound(int)
 *
 * 向socketchannel写数据
 * @see io.netty.channel.AbstractChannelHandlerContext#write(java.lang.Object, boolean, io.netty.channel.ChannelPromise)
 * @see io.netty.channel.AbstractChannelHandlerContext#invokeWrite0(java.lang.Object, io.netty.channel.ChannelPromise)
 *
 * @auther mac
 * @date 2019-12-27
 */
public class SimpleNettyServer {

    private static final EventExecutorGroup EVENT_EXECUTOR = new DefaultEventExecutorGroup(2);


    public static void main(String[] args) {
        /**
         * bossGroup 只是处理连接请求 , 真正的和客户端业务处理，
         * 会交给 workerGroup完成
         *
         * bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
         * 默认是 cpu核数 * 2
         **/
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class) //使用NioSocketChannel 作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                //handler对应 bossGroup , childHandler 对应 workerGroup
                .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象(匿名对象)
                    //给pipeline 设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 可以使用一个集合管理 SocketChannel， 再推送消息时，
                        // 可以将业务加入到各个channel 对应的NIOEventLoop 的 taskQueue
                        // 或者 scheduleTaskQueue
                        System.out.println("客户socketChannel hashcode=" + ch.hashCode());

                        //ch.pipeline().addLast(new WorkHandler());

                        // 使用指定的EVENT_EXECUTOR 执行WorkHandler
                        /**
                         * //取当前Handler(Context)的执行器
                         * EventExecutor executor = next.executor();
                         * // 当前线程和处理器的执行线程是否一致
                         * if (executor.inEventLoop()) {
                         *     next.invokeChannelRead(m);
                         * } else {不一致取Context(handler)指定的执行器
                         *     executor.execute(new Runnable() {
                         *         @Override
                         *         public void run() {
                         *             next.invokeChannelRead(m);
                         *         }
                         *     });
                         * }
                         * @see io.netty.channel.AbstractChannelHandlerContext#invokeChannelRead(io.netty.channel.AbstractChannelHandlerContext, java.lang.Object)
                         */
                        ch.pipeline().addLast(EVENT_EXECUTOR,new WorkHandler());
                    }
                }); // 给workerGroup 的 EventLoop 对应的管道设置处理器

        //启动服务器(并绑定端口)
        try {
            final ChannelFuture cf = bootstrap.bind(6668);
            //给cf 注册监听器
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                        System.out.println("服务端就绪...");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });
            //对关闭通道进行监听，主线程等待关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class WorkHandler extends ChannelInboundHandlerAdapter {
        //static final EventExecutorGroup EVENT_EXECUTOR = new DefaultEventExecutorGroup(16);
        /**
         * ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel等
         * Object msg: 就是客户端发送的数据 默认Object
         *
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            /**
             * 将业务任务提交给专门的业务处理线程池
             *
             * 如在业务中需要继续IO操作
             *
             * Netty将判断执行IO的线程与EventLoop是否是同一个线程
             * 不是这将当前操作封装为Task提交给EventLoop
             * @see io.netty.channel.AbstractChannelHandlerContext#write(java.lang.Object, boolean, io.netty.channel.ChannelPromise)
             * @see io.netty.util.concurrent.SingleThreadEventExecutor#inEventLoop(java.lang.Thread)
             *
             * if (executor.inEventLoop()) {
             *     if (flush) {
             *         next.invokeWriteAndFlush(m, promise);
             *     } else {
             *         next.invokeWrite(m, promise);
             *     }
             *  } else {
             *     final WriteTask task = WriteTask.newInstance(next, m, promise, flush);
             *     if (!safeExecute(executor, task, promise, m, !flush)) {
             *         // We failed to submit the WriteTask. We need to cancel it so we decrement the pending bytes
             *         // and put it back in the Recycler for re-use later.
             *         //
             *         // See https://github.com/netty/netty/issues/8343.
             *         task.cancel();
             *     }
             * }
             */
           /* EVENT_EXECUTOR.submit(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("异步执行任务1->线程:"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello--->, 客户端~(>^ω^<)喵", CharsetUtil.UTF_8));
            });*/

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步执行任务1->线程:"+Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("Hello--->, 客户端~(>^ω^<)喵", CharsetUtil.UTF_8));

            /* ctx.channel().eventLoop().execute(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("异步执行任务1->线程:"+Thread.currentThread().getName());
            });

            ctx.channel().eventLoop().execute(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("异步执行任务2->线程:"+Thread.currentThread().getName());
            });*/

            System.out.println("服务器读取线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());
            System.out.println("server ctx =" + ctx);
            Channel channel = ctx.channel();

            //将 msg 转成一个 ByteBuf
            ByteBuf buf = (ByteBuf) msg;
            System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
            System.out.println("客户端地址:" + channel.remoteAddress());
        }

        //数据读取完毕
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

            /**
             * writeAndFlush 是 write + flush
             * 将数据写入到缓存，并刷新,一般对这个发送的数据进行编码
             **/
            ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, 客户端~(>^ω^<)喵", CharsetUtil.UTF_8));
        }

        //处理异常, 一般是需要关闭通道
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}

