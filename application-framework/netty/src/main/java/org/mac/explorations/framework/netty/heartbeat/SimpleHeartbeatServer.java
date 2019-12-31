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

package org.mac.explorations.framework.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.AbstractScheduledEventExecutor;

import java.util.concurrent.TimeUnit;

/**
 * Netty心跳机制分析
 *
 * 连接建立(即注册channel完成) 调用Handler的handlerAdd方法开始
 * @see io.netty.channel.AbstractChannel.AbstractUnsafe#register0(io.netty.channel.ChannelPromise)
 *
 * IdleStateHandler 在handlerAdd 方法中执行初始化
 * @see io.netty.handler.timeout.IdleStateHandler#initialize(io.netty.channel.ChannelHandlerContext)
 * 记录上次读写时间
 *
 * 启动定时任务
 * @see AbstractScheduledEventExecutor#schedule(java.lang.Runnable, long, java.util.concurrent.TimeUnit)
 *
 * 超时判断任务
 * @see io.netty.handler.timeout.IdleStateHandler.ReaderIdleTimeoutTask#run(io.netty.channel.ChannelHandlerContext)
 * 已经超时设置新的超时任务 并调用超时时间处理器
 * 没有超时更新超时任务的超时时间
 *
 *
 * @auther mac
 * @date 2019-12-29
 */
public class SimpleHeartbeatServer {
    private static final int PORT = Integer.parseInt(System.getProperty("port","8888"));

    public void run () {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                             /**
                              * 1. IdleStateHandler 是netty 提供的处理空闲状态的处理器
                              * 2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
                              * 3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
                              * 4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接
                              * 5. 文档说明
                              *    triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                              *    read, write, or both operation for a while.
                              * 6. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个handler去处理
                              *
                              */
                            pipeline.addLast(new IdleStateHandler(3,5,10, TimeUnit.SECONDS));
                            pipeline.addLast(new IdleStateEventHandler());
                        }
                    });

            serverBootstrap.bind(PORT).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class IdleStateEventHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

            if(evt instanceof IdleStateEvent) {

                //将  evt 向下转型 IdleStateEvent
                IdleStateEvent event = (IdleStateEvent) evt;
                String eventType = null;
                switch (event.state()) {
                    case READER_IDLE:
                        eventType = "读空闲";
                        break;
                    case WRITER_IDLE:
                        eventType = "写空闲";
                        break;
                    case ALL_IDLE:
                        eventType = "读写空闲";
                        break;
                }

                System.out.println(ctx.channel().remoteAddress() + "--超时--" + eventType);
                //如果发生空闲，关闭通道
                //ctx.channel().close();
            }
        }
    }

    public static void main(String[] args) {
        new SimpleHeartbeatServer().run();
    }
}
