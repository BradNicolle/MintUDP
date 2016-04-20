package com.bradnicolle.mintudp.messages;

import com.bradnicolle.mintudp.common.Marshallable;

public class ConnectMessage implements Marshallable {
    private String name;

    public ConnectMessage() {}

    public ConnectMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public byte[] marshal() {
        return name.getBytes();
    }

    public ConnectMessage unmarshal(byte[] data) {
        return new ConnectMessage(new String(data));
    }
}
