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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CS模型中的server
 *
 * @auther mac
 * @date 2019-12-15
 */
public class Server {

    private ServerSocket serverSocket;
    /**
     * 客户端注册
     * 以客户端host:port为key 对应的Socket为值
     */
    private Map<String,Socket> registeredClients;

    private Thread serverMainThread;

    private ExecutorService executorService;

    public void start() {
        try {
            init();
            while (!serverMainThread.isInterrupted()) {
                Utils.logSimply("server wait accept client...");
                Socket clientSocket = serverSocket.accept();
                register (clientSocket);
                executorService.submit(new MessageHandler(clientSocket));
            }
        } catch (IOException e) {
            Utils.logSimply("server throw an exception:",e);
        } finally {
            destroy();
        }
    }

    private void destroy() {
        for (String key : registeredClients.keySet()) {
            Utils.release(registeredClients.get(key));
        }
        Utils.release(serverSocket);
        executorService.shutdown();
    }

    private void init() throws IOException {
        serverSocket = new ServerSocket(Constant.DEFAULT_SERVER_PORT);
        registeredClients = new ConcurrentHashMap<>();
        serverMainThread = Thread.currentThread();
        executorService = Executors.newFixedThreadPool(Constant.DEFAULT_THREAD_POOL_SIZE);
    }

    /**
     * 客户端注册
     *
     * @param clientSocket
     */
    private void register (Socket clientSocket) {
        if (clientSocket != null) {
            String clientId = getClientId(clientSocket);
            Utils.logSimply("client["+clientId+"] connect...");
            registeredClients.put(clientId,clientSocket);
        }
    }

    private String getClientId(Socket socket) {
        String host = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        return host+":"+port;
    }

    /**
     * 客户端移除
     */
    private void remove (String key) {
        Utils.logSimply("remove client:["+key+"]");
        registeredClients.remove(key);
    }

    public void shutdown() {
        serverMainThread.interrupt();
    }

    private class MessageHandler implements Runnable {

        private Socket socket;

        public MessageHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String message = null;
            BufferedReader clientEndpointMessageReader = null;
            try {
                clientEndpointMessageReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((message = clientEndpointMessageReader.readLine()) != null
                        && !Constant.COMMAND_LOGOUT.equalsIgnoreCase(message)) {
                    Utils.logSimply("client["+getClientId(socket)+"]:"+message);
                    forward("client["+getClientId(socket)+"]:"+message);
                }
            } catch (Exception e) {
                Utils.logSimply("forward message throw an exception:",e);
            } finally {
                remove(getClientId(socket));
                Utils.release(clientEndpointMessageReader,socket);
            }
        }

        /**
         * 向其他客户端转发消息
         *
         * @param message
         */
        private void forward(String message) throws IOException {
            String clientId = getClientId(socket);
            for (String client : registeredClients.keySet()) {
                if (!client.equals(clientId)) {
                    //System.out.println(message+" -> forward to:"+ key);
                    Socket forwardTo = registeredClients.get(client);
                    if (forwardTo != null && !forwardTo.isOutputShutdown()) {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(forwardTo.getOutputStream()));
                        writer.write(message + "\n");
                        writer.flush();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
