package com.lsdb.store.kv;

/**
 * Interface for an environment object. An environment object:
 * ~ creates and manages Buckets
 * ~ creates Transactions
 * ~ maintains database statistics
 * ~ and probably more...
 */
public interface Environment extends AutoCloseable {

    public Bucket openBucket(String bucketName);

    public Transaction startReadTxn();

    public Transaction startWriteTxn();

    public int getMaxKeySize();

    @Override
    public void close();

}