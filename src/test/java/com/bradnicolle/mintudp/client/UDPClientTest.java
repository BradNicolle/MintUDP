package com.bradnicolle.mintudp.client;

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

        UDPClient client = new UDPClient("localhost", 4445);
        //client.registerListener(StringMessage.class, (message) -> System.out.println("Message: " + ((StringMessage) message).getMessage() + "FIN"));
        //client.sendMessage(Constants.SERVER_ID, new StringMessage("hello"));

        client.sendConnectMessage("Geoff");
        client.sendConnectMessage("George");
    }

}
