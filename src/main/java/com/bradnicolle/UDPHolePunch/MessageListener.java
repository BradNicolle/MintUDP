package com.bradnicolle.UDPHolePunch;

public interface MessageListener<T extends Marshallable<T>> {
    void listen(Marshallable<T> marshallable);
    Class<T> getType();
}
