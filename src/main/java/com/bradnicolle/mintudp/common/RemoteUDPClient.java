package com.bradnicolle.mintudp.common;

import java.io.Serializable;

public class RemoteUDPClient implements Serializable {
    public String host;
    public String name;
    public int port;

    public RemoteUDPClient(String host, String name, int port) {
        this.host = host;
        this.name = name;
        this.port = port;
    }
}
