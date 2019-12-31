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

package org.mac.explorations.framework.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.time.LocalDateTime;

/**
 * @auther mac
 * @date 2019-12-29
 */
public class SimpleWebSocketServer {

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

                            //基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块方式写，添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             *  浏览器发送大量数据时，就会发出多次http请求
                             *  http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 1. WebSocket ，它的数据是以 帧(frame) 形式传递
                             * 2. 浏览器请求时 ws://localhost:7000/hello 表示请求的uri
                             * 3. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 (通过一个 状态码 101), 保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义的handler ，处理业务逻辑
                            pipeline.addLast(new SimpleTextWebSocketFrameHandler());
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

    private class SimpleTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

            System.out.println("服务器收到消息:" + msg.text());

            //回复消息
            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间[" + LocalDateTime.now() + "] " + msg.text()));
        }

        //当web客户端连接后， 触发方法
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            //id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一
            System.out.println("客户端连接 handlerAdded 被调用" + ctx.channel().id().asLongText());
            //System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
        }


        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端关闭 handlerRemoved 被调用" + ctx.channel().id().asLongText());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("异常发生 " + cause.getMessage());
            ctx.close(); //关闭连接
        }
    }

    public static void main(String[] args) {
        new SimpleWebSocketServer().run();
    }
}
