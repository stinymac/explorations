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

package org.mac.explorations.corejava.io.net.model.nio;

import org.mac.explorations.corejava.io.net.model.Constant;
import org.mac.explorations.corejava.io.net.model.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * CS模型中的server
 *
 * @auther mac
 * @date 2019-12-15
 */
public class Server {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private ByteBuffer rBuffer = ByteBuffer.allocate(Constant.DEFAULT_BUFFER_SIZE);
    private ByteBuffer wBuffer = ByteBuffer.allocate(Constant.DEFAULT_BUFFER_SIZE);

    private Thread serverMainThread;

    public void start() {
        try {
            init();
            while (!serverMainThread.isInterrupted()) {
                Utils.logSimply("server wait accept client...");
                // 询问一次 (阻塞)
                selector.select();
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (SelectionKey key : keySet) {
                    handle(key);
                }
                keySet.clear();
            }
        } catch (IOException e) {
            Utils.logSimply("server throw an exception:",e);
        } finally {
            destroy();
        }
    }

    private void init() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        // non-block
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(Constant.DEFAULT_SERVER_PORT));
        selector = Selector.open();
        // 监听serverSocketChannel的ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverMainThread = Thread.currentThread();
    }

    /**
     * 处理事件
     *
     * @param key
     */
    private void handle(SelectionKey key) throws IOException {
        // OP_ACCEPT
        if (key.isAcceptable()) {
            handleAccept(key);
        }
        // 客户端发送了消息
        else if (key.isReadable()) {
            handleRead(key);
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel clientSocketChannel = ssc.accept();
        clientSocketChannel.configureBlocking(false);
        clientSocketChannel.register(selector,SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();
        String message = receiveMessageFrom(clientSocketChannel);
        if (message.isEmpty() || Constant.COMMAND_LOGOUT.equalsIgnoreCase(message)) {
            key.cancel();
            selector.wakeup();
            if ( Constant.COMMAND_LOGOUT.equalsIgnoreCase(message)){
                Utils.logSimply("client["+getClientId(clientSocketChannel)+"] logout");
            }
        }
        else {
            Utils.logSimply("client["+getClientId(clientSocketChannel)+"]:"+message);
            forward(clientSocketChannel,message);
        }
    }

    private String getClientId(SocketChannel socketChannel) {
        Socket socket = socketChannel.socket();
        String host = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        return host+":"+port;
    }

    public void shutdown() {
        serverMainThread.interrupt();
    }

    private void destroy() {
        rBuffer.clear();
        wBuffer.clear();
        Utils.release(selector,serverSocketChannel);
    }

    /**
     * 读客户端消息
     *
     * @param socketChannel
     * @return
     */
    private String receiveMessageFrom(SocketChannel socketChannel) throws IOException {
        rBuffer.clear();
        while(socketChannel.read(rBuffer) > 0);
        rBuffer.flip();
        return String.valueOf(Constant.CHARSET.decode(rBuffer));
    }

    /**
     * 向其他客户端转发消息
     *
     * @param sendClient
     * @param message
     */
    private void forward(SocketChannel sendClient, String message) throws IOException {
        for (SelectionKey key: selector.keys()) {
            Channel currentChannel = key.channel();
            // 跳过 ServerSocketChannel
            if (currentChannel instanceof ServerSocketChannel || currentChannel.equals(sendClient)){
                continue;
            }
            if (key.isValid()) {
                wBuffer.clear();
                wBuffer.put(Constant.CHARSET.encode("["+getClientId((SocketChannel)currentChannel) + "]:" + message));
                wBuffer.flip();
                while (wBuffer.hasRemaining()) {
                    ((SocketChannel)currentChannel).write(wBuffer);
                }
            }
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
