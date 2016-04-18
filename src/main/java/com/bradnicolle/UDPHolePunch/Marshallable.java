package com.bradnicolle.UDPHolePunch;

public interface Marshallable {
    byte[] marshal();
    Marshallable unmarshal(byte[] data);
}
