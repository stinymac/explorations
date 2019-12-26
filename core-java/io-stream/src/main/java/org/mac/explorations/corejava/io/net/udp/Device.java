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
import java.util.UUID;

/**
 * @auther mac
 * @date 2019-12-21
 */
public class Device {

    private final String sn;
    private final Communicator communicator = new Communicator();

    public Device(String sn) {
        this.sn = sn;
    }

    private void start() {
        communicator.setDaemon(true);
        communicator.start();
    }

    private class Communicator extends Thread {

        private DatagramSocket datagramSocket;
        private final byte[] buffer = new byte[Constant.DEFAULT_BUFFER_SIZE];

        public void init() {
            try {
                // 监听约定端口接收广播消息
                datagramSocket = new DatagramSocket(Constant.DEFAULT_PORT);
            } catch (SocketException e) {
              throw new RuntimeException("listen port:"+Constant.DEFAULT_PORT+" fail",e);
            }
        }

        @Override
        public void run() {

            init();

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    DatagramPacket receivedPacket = receive();
                    int dataLength = receivedPacket.getLength();
                    String message = new String(receivedPacket.getData(), 0, dataLength);
                    System.out.printf("[Device: %s receive data : %s]%n",sn,message);
                    int port = MessageUtils.getPort(message);
                    if (port != -1) {
                        InetAddress address = receivedPacket.getAddress();
                        feedback(address,port);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        private DatagramPacket receive () throws IOException {
            System.out.printf("[Device: %s wait receive data...]%n",sn);
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            // 接收
            datagramSocket.receive(receivedPacket);
            return receivedPacket;
        }

        private void feedback(InetAddress address,int port) throws IOException {
            String deviceSN = MessageUtils.ofSN(sn);
            byte[] feedbackDataBytes = deviceSN.getBytes();
            DatagramPacket feedbackPacket = new DatagramPacket(feedbackDataBytes,
                    feedbackDataBytes.length,
                    address,
                    port);
            datagramSocket.send(feedbackPacket);
        }

        private void close() {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    public void stop() {
        communicator.interrupt();
    }

    @Override
    public String toString() {
        return "Device{" +
                " sn='" + sn + '\'' +
                '}';
    }

    public static void main(String[] args) throws IOException {
        int amount = 1;
        Set<Device> devices = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            Device device = new Device(UUID.randomUUID().toString().toUpperCase());
            devices.add(device);
            device.start();
        }
        System.out.println(" press any key to stop...");
        System.in.read();
        for (Device device : devices) {
            device.stop();
        }
    }
}
