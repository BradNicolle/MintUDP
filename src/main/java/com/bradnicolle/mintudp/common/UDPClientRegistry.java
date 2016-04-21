package com.bradnicolle.mintudp.common;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UDPClientRegistry implements Marshallable {
    private Map<Integer, RemoteUDPClient> registry = new HashMap<>();

    public UDPClientRegistry() {}

    public UDPClientRegistry(Map<Integer, RemoteUDPClient> registry) {
        this.registry = registry;
    }

    public Map<Integer, RemoteUDPClient> getList() {
        return registry;
    }

    public void printRegistry() {
        System.out.println("=== Registry ===");
        for (Integer clientId : registry.keySet()) {
            RemoteUDPClient client = registry.get(clientId);
            System.out.println(client.name + "@" + client.host + ":" + client.port);
        }
        System.out.println("----------------");
    }

    public byte[] marshal() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(registry);
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UDPClientRegistry unmarshal(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInput in = new ObjectInputStream(bis)) {
            Map<Integer, RemoteUDPClient> list = (Map<Integer, RemoteUDPClient>) in.readObject();
            return new UDPClientRegistry(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
