package com.bradnicolle.mintudp.messages;

import com.bradnicolle.mintudp.Marshallable;

public class StringMessage implements Marshallable {
    private String message;

    public StringMessage() {
        message = "";
    }

    public StringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public byte[] marshal() {
        return message.getBytes();
    }

    @Override
    public StringMessage unmarshal(byte[] data) {
        return new StringMessage(new String(data));
    }
}
