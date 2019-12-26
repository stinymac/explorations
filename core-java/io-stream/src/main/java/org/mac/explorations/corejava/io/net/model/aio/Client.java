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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.locks.LockSupport;

/**
 * CS模型中的client
 *
 * @auther mac
 * @date 2019-12-14
 */
public class Client {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private ByteBuffer rBuffer = ByteBuffer.allocate(Constant.DEFAULT_BUFFER_SIZE);
    private ByteBuffer wBuffer = ByteBuffer.allocate(Constant.DEFAULT_BUFFER_SIZE);
    private CompletionHandler<Integer,ByteBuffer> ioHandler;
    private Thread clientMainThread;

    public Client() {
    }

    private void init() throws IOException {
        asynchronousSocketChannel = AsynchronousSocketChannel.open();
        ioHandler = new IOHandler();
        clientMainThread = Thread.currentThread();
    }

    /**
     * 客户端启动
     */
    public void start() {
        try {
            init();
            Future<Void> future = asynchronousSocketChannel.connect(new InetSocketAddress(Constant.DEFAULT_SERVER_HOST,Constant.DEFAULT_SERVER_PORT));
            future.get();
            // 输入读取发送
            new UserInputHandler(Thread.currentThread()).start();
            while (!Thread.currentThread().isInterrupted()) {
                receive();
                LockSupport.park();
            }
        } catch (Exception e) {
            Utils.logSimply("start client fail:",e);
        } finally {
            shutdown();
        }
    }

    private class IOHandler implements CompletionHandler<Integer,ByteBuffer> {
        @Override
        public void completed(Integer len, ByteBuffer byteBuffer) {
            if (byteBuffer != null) { // 读(读数据时传入buffer作为attachment)
                if(len > 0) {
                    byteBuffer.flip();
                    String message = new String(byteBuffer.array(), Constant.CHARSET);
                    Utils.logSimply(message);

                    receive();
                }
            }
            else {
                Utils.logSimply("send message success!");
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            Utils.logSimply("Client io throw an exception:",exc);
        }
    }

    /**
     * 接受服务端消息
     *
     * @return
     */
    public void receive() {
        if (asynchronousSocketChannel != null && asynchronousSocketChannel.isOpen()) {
            asynchronousSocketChannel.read(rBuffer, rBuffer, ioHandler);
        }
    }


    /**
     * 向服务端发送消息
     *
     * @param msg
     */
    public void send(String msg) {
        if (!msg.isEmpty()) {
            wBuffer.clear();
            wBuffer.put(Constant.CHARSET.encode(msg));
            wBuffer.flip();
            if (asynchronousSocketChannel != null && asynchronousSocketChannel.isOpen()) {
                asynchronousSocketChannel.write(wBuffer, null, ioHandler);
            }
        }
    }

    /**
     * 客户端退出
     */
    public void shutdown () {
        send(Constant.COMMAND_LOGOUT);
        rBuffer.clear();
        wBuffer.clear();
        clientMainThread.interrupt();
        LockSupport.unpark(clientMainThread);
        Utils.release(asynchronousSocketChannel);
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
                while ((input = reader.readLine()) != null
                        && !Constant.COMMAND_LOGOUT.equalsIgnoreCase(input)) {
                    send(input);
                }
                clientMainThread.interrupt();
            } catch (Exception e) {
                Utils.logSimply("handle user input throw an exception:",e);
            } finally {
                Utils.release(reader);
            }
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}
