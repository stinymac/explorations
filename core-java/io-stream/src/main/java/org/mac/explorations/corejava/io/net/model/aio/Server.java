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

package org.mac.explorations.corejava.io.net.model.aio;

import org.mac.explorations.corejava.io.net.model.Constant;
import org.mac.explorations.corejava.io.net.model.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * CS模型中的server
 *
 * @auther mac
 * @date 2019-12-15
 */
public class Server {

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    private List<MessageHandler> clientMessageHandlers = new CopyOnWriteArrayList<>();
    private Thread serverMainThread;

    public void start() {
        try {
            init();
            while (!serverMainThread.isInterrupted()) {
                Utils.logSimply("server wait accept client...");
                asynchronousServerSocketChannel.accept(null,new AcceptorHandler());
                LockSupport.park();
            }
        } catch (IOException e) {
            Utils.logSimply("server throw an exception:",e);
        } finally {
            destroy();
        }
    }

    private void init() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(Constant.DEFAULT_THREAD_POOL_SIZE);
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
        asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
        asynchronousServerSocketChannel.bind(new InetSocketAddress(Constant.DEFAULT_SERVER_PORT));
        serverMainThread = Thread.currentThread();
    }

    private class AcceptorHandler implements CompletionHandler<AsynchronousSocketChannel,Object>{
        @Override
        public void completed(AsynchronousSocketChannel clientAsynchronousSocketChannel, Object attachment) {
            if (asynchronousServerSocketChannel.isOpen()) {
                // 等待下一客户端连接
                Utils.logSimply("server wait accept client...");
                asynchronousServerSocketChannel.accept(null,this);
            }
            Utils.logSimply("client["+getClientId(clientAsynchronousSocketChannel)+"] accepted.");
            //  新连接的在线客户端注册
            if (clientAsynchronousSocketChannel != null && clientAsynchronousSocketChannel.isOpen()) {
                MessageHandler messageHandler = new MessageHandler(clientAsynchronousSocketChannel);
                clientMessageHandlers.add(messageHandler);
                messageHandler.handle();
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            Utils.logSimply("Server accept client throw an exception:",exc);
        }
    }

    private class MessageHandler {

        private final AsynchronousSocketChannel clientAsynchronousSocketChannel;
        private final MessageHandler thisMessageHandler;
        private final IOHandler ioHandler;
        private final ByteBuffer buffer = ByteBuffer.allocate(Constant.DEFAULT_BUFFER_SIZE);

        public MessageHandler(AsynchronousSocketChannel channel) {
            this.clientAsynchronousSocketChannel = channel;
            thisMessageHandler = this;
            ioHandler = new IOHandler();
        }

        public void handle() {
            buffer.clear();
            clientAsynchronousSocketChannel.read(buffer, buffer,ioHandler);
        }

        private class IOHandler implements CompletionHandler<Integer, ByteBuffer> {
            @Override
            public void completed(Integer len, ByteBuffer byteBuffer) {
                if (byteBuffer != null) { // 读(读数据时传入buffer作为attachment)
                    if(len > 0) {
                        byteBuffer.flip();
                        String message =  new String(Constant.CHARSET.decode(byteBuffer).array());
                        Utils.logSimply("client["+getClientId(clientAsynchronousSocketChannel)+"]:"+message);
                        if (Constant.COMMAND_LOGOUT.equalsIgnoreCase(message)) {
                            clientMessageHandlers.remove(thisMessageHandler);
                        }
                        else {
                            forward(thisMessageHandler,byteBuffer,message);
                        }
                    }
                }
                else { // 写(转发消息)完成后继续读
                    handle();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                Utils.logSimply("Read message from client:["+getClientId(clientAsynchronousSocketChannel)+"] throw an exception:",exc);
            }

            private void forward(MessageHandler messageHandlerOfSendClient, ByteBuffer byteBuffer, String message) {
                for (MessageHandler messageHandler : clientMessageHandlers){
                    if (! messageHandler.equals(messageHandlerOfSendClient)) {
                        AsynchronousSocketChannel asynchronousSocketChannel = messageHandler.getClientAsynchronousSocketChannel();
                        if (asynchronousSocketChannel != null || asynchronousSocketChannel.isOpen()) {
                            byteBuffer.clear();
                            String forwardMessage = "["+getClientId(clientAsynchronousSocketChannel)+"]:"+message;
                            byteBuffer.put(Constant.CHARSET.encode(forwardMessage));
                            byteBuffer.flip();
                            asynchronousSocketChannel.write(byteBuffer,null,ioHandler);
                        }
                    }
                }
            }
        }

        public AsynchronousSocketChannel getClientAsynchronousSocketChannel() {
            return clientAsynchronousSocketChannel;
        }
    }


    private String getClientId(AsynchronousSocketChannel socketChannel) {
        try {
            InetSocketAddress socketAddress = (InetSocketAddress)socketChannel.getRemoteAddress();
            String host = socketAddress.getAddress().getHostAddress();
            int port = socketAddress.getPort();
            return host+":"+port;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void shutdown() {
        LockSupport.unpark(serverMainThread);
        serverMainThread.interrupt();
    }

    private void destroy() {
        Utils.release(asynchronousServerSocketChannel);
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
