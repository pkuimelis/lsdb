package com.lsdb.store.kv;

public class Util {

    public static class BytePair {

        public byte[] key;
        public byte[] val;

        public BytePair(byte[] key, byte[] val) {
            this.key = key;
            this.val = val;
        }
    }
}
