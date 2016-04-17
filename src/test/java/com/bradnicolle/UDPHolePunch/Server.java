package com.bradnicolle.UDPHolePunch;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server implements Runnable {
    private final int port;
    private boolean run = true;

    public Server(int port) {
        this.port = port;
    }

    public void kill() {
        run = false;
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port);

            while (run) {
                byte[] buf = new byte[512];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
