package com.bradnicolle.mintudp.server;

import com.bradnicolle.mintudp.common.RemoteUDPClient;
import com.bradnicolle.mintudp.common.UDPClientRegistry;
import com.bradnicolle.mintudp.common.Constants;
import com.bradnicolle.mintudp.common.Utils;
import com.bradnicolle.mintudp.messages.ConnectMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
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
    private boolean clean = false;

    private final int CONNECT_MESSAGE_HASH = Utils.hashClassName(ConnectMessage.class);
    private final int REGISTRY_CLEAN_DELAY = 60000;


    public UDPServer(int port, boolean clean) {
        this.port = port;
        executorService = Executors.newSingleThreadExecutor();
        scheduledRegistryClean = Executors.newSingleThreadScheduledExecutor();
        registry = new UDPClientRegistry();
    }

    public void start() throws SocketException {
        // TODO prevent from starting multiple servers here
        socket = new DatagramSocket(port);
        executorService.submit(new ServerRunnable());
        if (clean) scheduledRegistryClean.scheduleWithFixedDelay(() -> registry.getList().clear(), REGISTRY_CLEAN_DELAY, REGISTRY_CLEAN_DELAY, TimeUnit.MILLISECONDS);
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

                    // Server message
                    if (recipientId == Constants.SERVER_ID) {
                        int classNameHash = Utils.extractClassNameHash(buf);

                        if (classNameHash == CONNECT_MESSAGE_HASH) {
                            ConnectMessage message =
                                    new ConnectMessage().unmarshal(
                                            Utils.extractMessageBytes(buf, packet.getLength()));

                            String hostAddress = packet.getAddress().getHostAddress();
                            registry.getList().put(Utils.hashString(message.getName()), new RemoteUDPClient(hostAddress, message.getName(), packet.getPort()));
                            registry.printRegistry();

                            // We can send -1 as recipientId because client doesn't check this
                            byte[] sendBuf = Utils.generateSendMessageBuffer(-1, registry);
                            DatagramPacket sendPacket =
                                    new DatagramPacket(sendBuf, sendBuf.length, packet.getAddress(), packet.getPort());
                            socket.send(sendPacket);
                        }
                    }

                    // Client message, just route it to the desired client
                    else {
                        Map<Integer, RemoteUDPClient> reg = registry.getList();
                        RemoteUDPClient client = reg.get(recipientId);
                        if (client != null) {
                            DatagramPacket sendPacket =
                                    new DatagramPacket(buf, packet.getLength(), InetAddress.getByName(client.host), client.port);
                            socket.send(sendPacket);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
