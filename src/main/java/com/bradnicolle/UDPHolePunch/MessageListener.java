package com.bradnicolle.UDPHolePunch;

@FunctionalInterface
public interface MessageListener {
    void listen(Marshallable message);
}
