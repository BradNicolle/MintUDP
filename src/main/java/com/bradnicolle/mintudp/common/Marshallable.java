package com.bradnicolle.mintudp.common;

public interface Marshallable {
    byte[] marshal();
    Marshallable unmarshal(byte[] data);
}
