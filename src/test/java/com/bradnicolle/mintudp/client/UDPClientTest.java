package com.bradnicolle.mintudp.client;

import com.bradnicolle.mintudp.common.UDPClientRegistry;
import com.bradnicolle.mintudp.messages.StringMessage;
import com.bradnicolle.mintudp.server.UDPServer;
import org.junit.Test;

public class UDPClientTest {

    @Test
    public void test() throws Exception {
        StringMessage t = new StringMessage("hello");
        System.out.println(t.getMessage());

        UDPServer server = new UDPServer(4445);
        server.start();

        UDPClient client1 = new UDPClient("localhost", 4445);
        UDPClient client2 = new UDPClient("localhost", 4445);
        //client.registerListener(StringMessage.class, (message) -> System.out.println("Message: " + ((StringMessage) message).getMessage() + "FIN"));
        //client.sendMessage(Constants.SERVER_ID, new StringMessage("hello"));
        client1.registerListener(StringMessage.class, (message) -> System.out.println("Geoff received message: " + ((StringMessage) message).getMessage() + "FIN"));
        client1.registerListener(UDPClientRegistry.class, (message) -> {
            System.out.println("REGISTRY RECEIVED!!");
            ((UDPClientRegistry) message).printRegistry();
        });
        client2.registerListener(StringMessage.class, (message) -> System.out.println("George received message: " + ((StringMessage) message).getMessage() + "FIN"));

        client2.sendConnectMessage("George");
        client1.sendConnectMessage("Geoff");

        client1.sendMessage("George", new StringMessage("Hello George"));
        client2.sendMessage("Geoff", new StringMessage("I am the lolrus!"));

        Thread.sleep(2000);
    }

}
