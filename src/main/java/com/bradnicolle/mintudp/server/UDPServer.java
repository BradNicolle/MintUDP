package com.bradnicolle.mintudp.server;

import com.bradnicolle.mintudp.common.RemoteUDPClient;
import com.bradnicolle.mintudp.common.UDPClientRegistry;
import com.bradnicolle.mintudp.common.Constants;
import com.bradnicolle.mintudp.common.Utils;
import com.bradnicolle.mintudp.messages.ConnectMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UDPServer {
    private final int port;
    private DatagramSocket socket;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledRegistryClean;
    private UDPClientRegistry registry;
    private boolean running = true;

    private final int CONNECT_MESSAGE_HASH = Utils.hashClassName(ConnectMessage.class);


    public UDPServer(int port) {
        this.port = port;
        executorService = Executors.newSingleThreadExecutor();
        scheduledRegistryClean = Executors.newSingleThreadScheduledExecutor();
        registry = new UDPClientRegistry();
    }

    public void start() throws SocketException {
        // TODO prevent from starting multiple servers here
        socket = new DatagramSocket(port);
        executorService.submit(new ServerRunnable());
        scheduledRegistryClean.scheduleWithFixedDelay(() -> registry.getList().clear(), 1000, 2000, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        running = false;
        executorService.shutdown();
    }

    private class ServerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (running) {
                    System.out.println("Listening for new packet...");
                    byte[] buf = new byte[Constants.MAX_PACKET_SIZE];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    int recipientId = Utils.extractRecipientId(buf);
                    if (recipientId == Constants.SERVER_ID) {
                        System.out.println("I am the recipient");
                        int classNameHash = Utils.extractClassNameHash(buf);
                        if (classNameHash == CONNECT_MESSAGE_HASH) {
                            System.out.println("Connect message received");
                            ConnectMessage message =
                                    new ConnectMessage().unmarshal(
                                            Utils.extractMessageBytes(buf, packet.getLength()));
                            String hostAddress = packet.getAddress().getHostAddress();
                            registry.getList().add(new RemoteUDPClient(hostAddress, message.getName()));
                            registry.printRegistry();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
