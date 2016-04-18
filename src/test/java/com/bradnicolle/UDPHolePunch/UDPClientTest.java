package com.bradnicolle.UDPHolePunch;

import org.junit.Test;

import java.util.concurrent.Future;

public class UDPClientTest {

    @Test
    public void test() throws Exception {
        /*UDPClient client = new UDPClient("localhost", 4445);
        Future<UDPClientRegistry> registry = client.getRegistry("me");
        registry.get();*/
    }

    private class TestMessage implements Marshallable {
        private String message;

        public TestMessage(String message) {
            this.message = message;
        }

        @Override
        public byte[] marshal() {
            return message.getBytes();
        }

        @Override
        public TestMessage unmarshal(byte[] data) {
            return new TestMessage(new String(data));
        }
    }

}
