package com.bradnicolle.UDPHolePunch;

import org.junit.Test;

public class UDPClientTest {

    @Test
    public void test() throws Exception {
        UDPClient client = new UDPClient("localhost", 4445);
        client.printListeners();
        client.registerListener(new MessageListener() {
            @Override
            public void listen(Marshallable marshallable) {

            }

            @Override
            public Class<? extends Marshallable> getType() {
                return TestMessage.class;
            }
        });
    }

    private class TestMessage implements Marshallable<TestMessage> {
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
