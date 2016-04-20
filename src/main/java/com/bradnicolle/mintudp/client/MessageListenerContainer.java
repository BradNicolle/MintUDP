package com.bradnicolle.mintudp.client;

import com.bradnicolle.mintudp.common.Marshallable;

public class MessageListenerContainer {
    private Class<? extends Marshallable> type;
    private MessageListener listener;

    public MessageListenerContainer(Class<? extends Marshallable> type, MessageListener listener) {
        this.type = type;
        this.listener = listener;
    }

    public Class<? extends Marshallable> getType() {
        return type;
    }

    public MessageListener getListener() {
        return listener;
    }
}
