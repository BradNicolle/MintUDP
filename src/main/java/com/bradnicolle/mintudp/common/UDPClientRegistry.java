package com.bradnicolle.mintudp.common;

import java.util.ArrayList;
import java.util.List;

public class UDPClientRegistry implements Marshallable {
    private List<RemoteUDPClient> registry = new ArrayList<>();

    public UDPClientRegistry() {}

    public UDPClientRegistry(List<RemoteUDPClient> registry) {
        this.registry = registry;
    }

    public List<RemoteUDPClient> getList() {
        return registry;
    }

    public void printRegistry() {
        System.out.println("=== Registry ===");
        for (RemoteUDPClient client : registry) {
            System.out.println(client.name + "@" + client.host);
        }
        System.out.println("----------------");
    }

    public byte[] marshal() {
        return null;
    }

    public UDPClientRegistry unmarshal(byte[] data) {
        List<RemoteUDPClient> reg = new ArrayList<>();
        UDPClientRegistry reg2 = new UDPClientRegistry(reg);
        return reg2;
    }
}
