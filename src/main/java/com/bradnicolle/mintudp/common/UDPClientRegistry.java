package com.bradnicolle.mintudp.common;

import java.io.*;
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
            List<RemoteUDPClient> list = (List<RemoteUDPClient>) in.readObject();
            return new UDPClientRegistry(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
