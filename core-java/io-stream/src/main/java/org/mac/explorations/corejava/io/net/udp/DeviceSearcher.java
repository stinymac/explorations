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

package org.mac.explorations.corejava.io.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @auther mac
 * @date 2019-12-21
 */
public class DeviceSearcher {

    private final Set<SearchedDevice> devices = new HashSet<>();
    private final CountDownLatch latch = new CountDownLatch(1);
    DeviceFeedbackListener deviceFeedbackListener = new DeviceFeedbackListener();

    public void start() {
        try {
            deviceFeedbackListener.setDaemon(true);
            deviceFeedbackListener.start();
            latch.await();
            System.out.println("Device feedback listener start.");
            broadcastSearcherMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        deviceFeedbackListener.interrupt();
    }

    public Set<SearchedDevice> getDevices() {
        return devices;
    }

    private class DeviceFeedbackListener extends Thread {

        private DatagramSocket datagramSocket = null;
        private final byte[] buffer = new byte[Constant.DEFAULT_BUFFER_SIZE];

        public void init() {
            try {
                datagramSocket = new DatagramSocket(Constant.SEARCHER_LISTEN_PORT);
            } catch (SocketException e) {
                throw new RuntimeException("listen port:"+Constant.SEARCHER_LISTEN_PORT+" fail",e);
            }
        }

        @Override
        public void run() {
            try {
                init();
                latch.countDown();
                while (!Thread.currentThread().isInterrupted()) {
                    // 阻塞
                    DatagramPacket feedbackMessagePacket = receive();
                    updateSearchedDevice(feedbackMessagePacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        private void updateSearchedDevice(DatagramPacket feedbackMessagePacket) {

            int dataLength = feedbackMessagePacket.getLength();
            String message = new String(feedbackMessagePacket.getData(), 0, dataLength);

            if (message != null && message.trim().length() != 0) {
                System.out.printf("[A device was searched]%n");
                String deviceSN = MessageUtils.getSN(message);
                String ip = feedbackMessagePacket.getAddress().getHostAddress();
                int port = feedbackMessagePacket.getPort();

                devices.add(new SearchedDevice(deviceSN,ip,port));
            }
        }

        private DatagramPacket receive () throws IOException {
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            // 接收
            datagramSocket.receive(receivedPacket);
            return receivedPacket;
        }

        private void close() {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    /**
     * 设备查找信息广播
     */
    private void broadcastSearcherMessage() throws IOException {
        DatagramSocket ds = new DatagramSocket();
        // 构建一份请求数据
        String searcherMessage = MessageUtils.ofPort(Constant.SEARCHER_LISTEN_PORT);
        byte[] searcherMessageBytes = searcherMessage.getBytes();
        // 直接构建packet
        DatagramPacket searcherMessagePacket = new DatagramPacket(searcherMessageBytes,
                searcherMessageBytes.length);
        // 广播地址 20000端口
        searcherMessagePacket.setAddress(InetAddress.getByName(Constant.BROAD_CAST_ADDRESS));
        searcherMessagePacket.setPort(Constant.DEFAULT_PORT);
        // 发送
        System.out.println("broadcast message search device...");
        ds.send(searcherMessagePacket);
        ds.close();
    }

    private class SearchedDevice {
        final int port;
        final String ip;
        final String sn;

        private SearchedDevice(String sn, String ip,int port) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        DeviceSearcher searcher = new DeviceSearcher();
        searcher.start();
        System.in.read();
        searcher.stop();
        for (SearchedDevice device : searcher.getDevices()) {
            System.out.println(device);
        }
    }
}
