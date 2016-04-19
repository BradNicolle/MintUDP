package com.bradnicolle.mintudp;

@FunctionalInterface
public interface MessageListener {
    void listen(Marshallable message);
}
