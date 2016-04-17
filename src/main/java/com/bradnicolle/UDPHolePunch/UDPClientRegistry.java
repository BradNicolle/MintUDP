package com.bradnicolle.UDPHolePunch;

import java.util.List;

public class UDPClientRegistry implements Marshallable<UDPClientRegistry> {
    List<RemoteUDPClient> registry;

    public List<RemoteUDPClient> getList() {
        return registry;
    }

    public byte[] marshal() {
        return new byte[0];
    }

    public UDPClientRegistry unmarshal(byte[] data) {
        return null;
    }
}
