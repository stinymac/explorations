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

package org.mac.explorations.corejava.io.net.model.bio;

import org.mac.explorations.corejava.io.net.model.Constant;
import org.mac.explorations.corejava.io.net.model.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * CS模型中的client
 *
 * @auther mac
 * @date 2019-12-14
 */
public class Client {

    private Socket socket;
    private BufferedReader serverEndpointMessageReader;
    private BufferedWriter clientEndpointMessageWriter;

    public Client() {
    }

    private void init() throws IOException {
        socket = new Socket(Constant.DEFAULT_SERVER_HOST, Constant.DEFAULT_SERVER_PORT);
        serverEndpointMessageReader = getReader();
        clientEndpointMessageWriter = getWriter();
    }

    /**
     * 客户端启动
     */
    public void start() {
        try {
            init();
            Thread clientMainThread = Thread.currentThread();
            // 输入读取发送
            new UserInputHandler(clientMainThread).start();
            // 消息接收展示
            new ServerEndpointMessageHandler().start();
            while (!clientMainThread.isInterrupted()) {

            }
        } catch (Exception e) {
            Utils.logSimply("start client fail:",e);
        } finally {
            try {
                shutdown();
            } catch (IOException e) {
                Utils.logSimply("shutdown client fail:",e);
            }
        }
    }

    /**
     * 向服务端发送消息
     *
     * @param msg
     */
    public void send(String msg)  throws IOException{
        if (socket.isOutputShutdown()) {
            return;
        }

        clientEndpointMessageWriter.write(msg + "\n");
        clientEndpointMessageWriter.flush();
    }

    /**
     * 接受服务端消息
     *
     * @return
     */
    public String receive()  throws IOException {
        if (socket.isInputShutdown()) {
            return null;
        }
        String message = serverEndpointMessageReader.readLine();
        return message;
    }

    /**
     * 客户端退出
     */
    public void shutdown () throws IOException  {
        send(Constant.COMMAND_LOGOUT);
        Utils.release(clientEndpointMessageWriter,serverEndpointMessageReader,socket);
    }

    private BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private BufferedWriter getWriter() throws IOException {
        return new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
            String input = null;
            try {

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

    private class ServerEndpointMessageHandler extends Thread {

        public ServerEndpointMessageHandler() {
            this.setDaemon(true);
        }

        @Override
        public void run() {
            handle();
        }

        private void handle() {
            try {
                while (true) {
                    String msg = receive();
                    System.out.println(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}
