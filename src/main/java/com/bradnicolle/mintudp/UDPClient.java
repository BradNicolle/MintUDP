package com.bradnicolle.mintudp;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UDPClient {
    public static final int MAX_PACKET_SIZE = 512;
    private Map<Integer, MessageListenerContainer> listeners;
    private final DatagramSocket socket;
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    public UDPClient(String address, int port) throws SocketException, UnknownHostException {
        listeners = new ConcurrentHashMap<>();
        socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(address), port);
        ExecutorService receiveService = Executors.newSingleThreadExecutor();
        ReceiveRunnable receiveRunnable = new ReceiveRunnable();
        receiveService.submit(receiveRunnable);
    }

    public UDPClient registerListener(Class<? extends Marshallable> type, MessageListener listener) {
        listeners.put(hashClassName(type), new MessageListenerContainer(type, listener));
        return this;
    }

    public void printListeners() {
        for (Integer i : listeners.keySet()) {
            System.out.println(i + " " + listeners.get(i).getType());
        }
    }

    public Future<UDPClientRegistry> getRegistry(String name) throws IOException, PacketLengthExceededException {
        sendMessage(new ConnectionRequest(name));
        return pool.submit(() -> {
            byte[] buf = new byte[MAX_PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            return new UDPClientRegistry().unmarshal(buf);
        });
    }

    public void sendMessage(Marshallable message) throws IOException, PacketLengthExceededException {
        byte[] typeBuf = ByteBuffer.allocate(4)
                            .order(ByteOrder.BIG_ENDIAN)
                            .putInt(hashClassName(message.getClass()))
                            .array();
        byte[] messageBuf = message.marshal();
        byte[] buf = new byte[typeBuf.length + messageBuf.length];

        if (buf.length > MAX_PACKET_SIZE) {
            throw new PacketLengthExceededException(buf.length);
        }

        System.arraycopy(typeBuf, 0, buf, 0, typeBuf.length);
        System.arraycopy(messageBuf, 0, buf, typeBuf.length, messageBuf.length);

        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.send(packet);
    }

    private int hashClassName(Class clazz) {
        String className = clazz.getName();
        // See http://stackoverflow.com/questions/2624192/good-hash-function-for-strings
        int hash = 7;
        for (int i = 0; i < className.length(); i++) {
            hash = hash*31 + className.charAt(i);
        }
        return hash;
    }

    public class PacketLengthExceededException extends Exception {
        public PacketLengthExceededException(int length) {
            super("Packet length exceeded, max length is " + MAX_PACKET_SIZE + " bytes, yours was " + length);
        }
    }

    private class ConnectionRequest implements Marshallable {
        private String name;

        public ConnectionRequest(String name) {
            this.name = name;
        }

        public byte[] marshal() {
            return name.getBytes();
        }

        public ConnectionRequest unmarshal(byte[] data) {
            return new ConnectionRequest(new String(data));
        }
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
                    byte[] buf = new byte[MAX_PACKET_SIZE];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);

                    socket.receive(packet);

                    int classNameHash = ByteBuffer.wrap(buf, 0, 4).getInt();
                    MessageListenerContainer listener = listeners.get(classNameHash);

                    if (listener != null) {
                        Class<? extends Marshallable> messageClass = listener.getType();
                        try {
                            Marshallable message = messageClass.newInstance();
                            byte[] messageBytes = Arrays.copyOfRange(buf, 4, packet.getLength());
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
