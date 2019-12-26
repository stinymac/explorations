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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * CS模型中的client
 *
 * @auther mac
 * @date 2019-12-14
 */
public class Client {

    private SocketChannel socketChannel;
    private Selector selector;
    private ByteBuffer rBuffer = ByteBuffer.allocate(Constant.DEFAULT_BUFFER_SIZE);
    private ByteBuffer wBuffer = ByteBuffer.allocate(Constant.DEFAULT_BUFFER_SIZE);

    public Client() {
    }

    private void init() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress(Constant.DEFAULT_SERVER_HOST,Constant.DEFAULT_SERVER_PORT));
    }

    /**
     * 客户端启动
     */
    public void start() {
        try {
            init();
            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    handle(key);
                }
                selectionKeys.clear();
            }
        } catch (Exception e) {
            Utils.logSimply("start client fail:",e);
        } finally {
            try {
                shutdown();
            } catch (ClosedChannelException e) {
                Utils.logSimply("shutdown client");
            } catch (IOException e) {
                Utils.logSimply("shutdown client fail:",e);
            }
        }
    }

    private void handle(SelectionKey key) throws ClosedChannelException,IOException {
        if (key.isConnectable()) {
            handleConnection(key);
        }
        else if (key.isReadable()) {
           String message = receive((SocketChannel)key.channel());
           Utils.logSimply(message);
        }
    }

    private void handleConnection(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();
            // 输入读取发送
            new UserInputHandler(Thread.currentThread()).start();
        }
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 向服务端发送消息
     *
     * @param msg
     */
    public void send(String msg)  throws IOException{
        if (msg.isEmpty()) {
            return;
        }
        wBuffer.clear();
        wBuffer.put(Constant.CHARSET.encode(msg));
        wBuffer.flip();
        while (wBuffer.hasRemaining()) {
            socketChannel.write(wBuffer);
        }
    }

    /**
     * 接受服务端消息
     *
     * @return
     */
    public String receive(SocketChannel socketChannel)  throws IOException {
        rBuffer.clear();
        while(socketChannel.read(rBuffer) > 0);
        rBuffer.flip();
        return String.valueOf(Constant.CHARSET.decode(rBuffer));
    }

    /**
     * 客户端退出
     */
    public void shutdown () throws IOException  {
        send(Constant.COMMAND_LOGOUT);
        rBuffer.clear();
        wBuffer.clear();
        Utils.release(selector,socketChannel);
    }

    private class UserInputHandler extends Thread {

        private final Thread clientMainThread;

        public UserInputHandler(Thread clientMainThread) {
            this.clientMainThread = clientMainThread;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            handle();
        }

        private void handle () {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String input;
                while ((input = reader.readLine()) != null && !Constant.COMMAND_LOGOUT.equalsIgnoreCase(input)) {
                    send(input);
                }
                clientMainThread.interrupt();
            } catch (Exception e) {
                Utils.logSimply("handle user input throw exception:",e);
            } finally {
                Utils.release(reader);
            }
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}
