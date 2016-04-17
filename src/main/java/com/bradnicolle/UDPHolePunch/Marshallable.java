package com.bradnicolle.UDPHolePunch;

public interface Marshallable<T> {
    byte[] marshal();
    T unmarshal(byte[] data);
}
