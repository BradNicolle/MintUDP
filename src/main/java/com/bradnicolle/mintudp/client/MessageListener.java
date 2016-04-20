package com.bradnicolle.mintudp.client;

import com.bradnicolle.mintudp.common.Marshallable;

@FunctionalInterface
public interface MessageListener {
    void listen(Marshallable message);
}
