package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Transaction;
import org.lmdbjava.Txn;

import java.nio.ByteBuffer;

/**
 * Implementation of the Transaction interface using the LMDB-Java transaction API.
 */
public class LmdbTransaction implements Transaction {

    private final Txn<ByteBuffer> txn;

    public LmdbTransaction(Txn<ByteBuffer> txn) {
        this.txn = txn;
    }

    @Override
    public void commit() {
        txn.commit();
    }

    @Override
    public void abort() {
        txn.abort();
    }

    @Override
    public void close() {
        txn.close();
    }

    public Txn<ByteBuffer> raw() {
        return txn;
    }
}