package com.bradnicolle.UDPHolePunch;

public class RemoteUDPServer {
    private String address;
    private int port;

    public RemoteUDPServer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
