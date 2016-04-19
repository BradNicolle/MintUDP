package com.bradnicolle.mintudp;

public interface Marshallable {
    byte[] marshal();
    Marshallable unmarshal(byte[] data);
}
