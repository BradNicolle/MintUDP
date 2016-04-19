package com.bradnicolle.mintudp;

import com.bradnicolle.mintudp.messages.StringMessage;
import org.junit.Test;

public class UDPClientTest {

    @Test
    public void test() throws Exception {
        StringMessage t = new StringMessage();
        System.out.println(t.getMessage());
        // Requires appropriate server running on localhost:4445
        UDPClient client = new UDPClient("localhost", 4445);
        client.registerListener(StringMessage.class, (message) -> System.out.println("Message: " + ((StringMessage) message).getMessage() + "FIN"));
        client.sendMessage(new StringMessage("hello"));
        Thread.sleep(2000);
    }

}
