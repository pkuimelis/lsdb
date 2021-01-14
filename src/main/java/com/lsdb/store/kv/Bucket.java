package com.lsdb.store.kv;

/**
 * Interface for a persistent data structure that supports efficient point and range queries.
 */
public interface Bucket {

    public byte[] get(byte[] key);

    public void put(byte[] key, byte[] val);

    public byte[] get(Transaction txn, byte[] key);

    public void put(Transaction txn, byte[] key, byte[] val);

    public Iterable<Util.BytePair> iterateAll(Transaction txn);

    public Iterable<Util.BytePair> iterateRange(Transaction txn, byte[] startKey, byte[] endKey);

}