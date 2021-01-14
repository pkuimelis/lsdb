package com.lsdb.store.kv.lmdb;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Util {

    // Copy data from direct buffer -> preallocated byte array.
    public static void buff2byte(ByteBuffer buf, byte[] bytes) {
        buf.clear();
        buf.get(bytes, 0, bytes.length);
    }

    // Copy data from a direct buffer -> new byte array.
    public static byte[] buff2byte(ByteBuffer buf) {
        buf.clear();
        byte[] bytes = new byte[buf.capacity()];
        buf.get(bytes, 0, bytes.length);
        return bytes;
    }

    // Copy data from byte array -> preallocated direct buffer.
    public static void byte2buff(byte[] bytes, ByteBuffer buf) {
        buf.put(bytes).flip();
    }

    // Copy data from byte array -> new direct buffer.
    public static ByteBuffer byte2buff(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length);
        buf.put(bytes).flip();
        return buf;
    }

    public static String bytes2str(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
