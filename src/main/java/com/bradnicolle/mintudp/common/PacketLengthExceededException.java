package com.bradnicolle.mintudp.common;

public class PacketLengthExceededException extends Exception {
    public PacketLengthExceededException(int length) {
        super("Packet length exceeded, max length is " + Constants.MAX_PACKET_SIZE + " bytes, yours was " + length);
    }
}
