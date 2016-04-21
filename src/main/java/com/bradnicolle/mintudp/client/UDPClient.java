package com.bradnicolle.mintudp.client;

import com.bradnicolle.mintudp.common.*;
import com.bradnicolle.mintudp.messages.ConnectMessage;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPClient {
    private Map<Integer, MessageListenerContainer> listeners;
    private final DatagramSocket socket;


    public UDPClient(String address, int port) throws SocketException, UnknownHostException {
        listeners = new ConcurrentHashMap<>();
        socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(address), port);
        ExecutorService receiveService = Executors.newSingleThreadExecutor();
        ReceiveRunnable receiveRunnable = new ReceiveRunnable();
        receiveService.submit(receiveRunnable);
    }

    public UDPClient registerListener(Class<? extends Marshallable> type, MessageListener listener) {
        listeners.put(Utils.hashClassName(type), new MessageListenerContainer(type, listener));
        return this;
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void printListeners() {
        for (Integer i : listeners.keySet()) {
            System.out.println(i + " " + listeners.get(i).getType());
        }
    }

    public void sendConnectMessage(String name) throws IOException, PacketLengthExceededException {
        sendMessage(Constants.SERVER_ID, new ConnectMessage(name));
    }

    public void sendMessage(String recipientId, Marshallable message) throws IOException, PacketLengthExceededException {
        sendMessage(Utils.hashString(recipientId), message);
    }

    public void sendMessage(int recipientId, Marshallable message) throws IOException, PacketLengthExceededException {
        byte[] buf = Utils.generateSendMessageBuffer(recipientId, message);

        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.send(packet);
    }

    private class ReceiveRunnable implements Runnable {
        private boolean running = true;

        public void kill() {
            running = false;
        }

        @Override
        public void run() {
            while(running) {
                try {
                    byte[] buf = new byte[Constants.MAX_PACKET_SIZE];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);

                    socket.receive(packet);

                    int classNameHash = Utils.extractClassNameHash(buf);
                    MessageListenerContainer listener = listeners.get(classNameHash);

                    if (listener != null) {
                        Class<? extends Marshallable> messageClass = listener.getType();
                        try {
                            Marshallable message = messageClass.newInstance();
                            byte[] messageBytes = Utils.extractMessageBytes(buf, packet.getLength());
                            listener.getListener().listen(message.unmarshal(messageBytes));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
