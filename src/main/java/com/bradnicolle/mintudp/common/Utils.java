package com.bradnicolle.mintudp.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Utils {

    public static int extractRecipientId(byte[] data) {
        // Recipient ID is first 4 bytes of message
        return ByteBuffer.wrap(data, 0, 4).getInt();
    }

    public static int extractClassNameHash(byte[] data) {
        // Class name hash is second group of 4 bytes
        return ByteBuffer.wrap(data, 4, 4).getInt();
    }

    public static byte[] extractMessageBytes(byte[] data, int length) {
        return Arrays.copyOfRange(data, 8, length);
    }

    public static byte[] int2byteArr(int data) {
        return ByteBuffer.allocate(4)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(data)
                .array();
    }

    public static int hashClassName(Class clazz) {
        String className = clazz.getName();
        return hashString(className);
    }

    public static int hashString(String str) {
        // See http://stackoverflow.com/questions/2624192/good-hash-function-for-strings
        int hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = hash*31 + str.charAt(i);
        }
        return hash;
    }
}
