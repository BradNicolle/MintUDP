package com.bradnicolle.UDPHolePunch;

import java.util.ArrayList;
import java.util.List;

public class UDPClientRegistry implements Marshallable {
    private List<RemoteUDPClient> registry = new ArrayList<>();

    public UDPClientRegistry() {

    }

    public UDPClientRegistry(List<RemoteUDPClient> registry) {
        this.registry = registry;
    }

    public List<RemoteUDPClient> getList() {
        return registry;
    }

    public byte[] marshal() {
        return null;
    }

    public UDPClientRegistry unmarshal(byte[] data) {
        List<RemoteUDPClient> reg = new ArrayList<>();
        reg.add(new RemoteUDPClient());
        UDPClientRegistry reg2 = new UDPClientRegistry(reg);
        return reg2;
    }
}
