package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Bucket;
import com.lsdb.store.kv.Environment;
import com.lsdb.store.kv.Transaction;
import com.lsdb.store.kv.Util.BytePair;

import org.lmdbjava.Dbi;
import org.lmdbjava.KeyRange;
import org.lmdbjava.KeyRangeType;
import org.lmdbjava.Txn;

import java.nio.ByteBuffer;

/**
 * Implementation of the Bucket interface using the LMDB-Java database API.
 */
public class LmdbBucket implements Bucket {

    private final Environment env;
    private final Dbi<ByteBuffer> db;

    // LMDB-Java requires that keys be passed as Direct buffers.
    // Since allocating these buffers is costly, we reuse them when possible.
    private final ThreadLocal<ByteBuffer> keyBuffer = new ThreadLocal<>();

    public LmdbBucket(Environment env, Dbi<ByteBuffer> db) {
        this.env = env;
        this.db = db;
    }

    @Override
    public byte[] get(byte[] key) {
        try (Transaction txn = env.startReadTxn()) {
            return get(txn, key);
        }
    }

    @Override
    public void put(byte[] key, byte[] val) {
        try (Transaction txn = env.startWriteTxn()) {
            put(txn, key, val);
            txn.commit();
        }
    }

    @Override
    public byte[] get(Transaction txn, byte[] key) {
        ByteBuffer keyBuf = keyBuffer.get();
        Util.byte2buff(key, keyBuf);
        ByteBuffer valBuf = db.get(unwrap(txn), keyBuf);
        return valBuf != null ? Util.buff2byte(valBuf) : null;
    }

    @Override
    public void put(Transaction txn, byte[] key, byte[] val) {
        ByteBuffer keyBuf = getKeyBuffer();
        Util.byte2buff(key, keyBuf);
        try {
            ByteBuffer valBuf = db.reserve(unwrap(txn), keyBuf, val.length);
            valBuf.put(val);
            valBuf.flip();
        } finally {
            keyBuf.clear();
        }
    }

    @Override
    public Iterable<BytePair> iterateAll(Transaction txn) {
        return new LmdbBucketIterator(db.iterate(unwrap(txn), KeyRange.all()));
    }

    /**
     * The range iterator works as follows: assume the table has keys [ 2, 4, 6, 8 ]
     * - if range is 3 to 7, returned keys are [ 4, 6 ].
     * - if range is 2 to 6, returned keys are [ 2, 4, 6 ].
     */
    @Override
    public Iterable<BytePair> iterateRange(Transaction txn, byte[] startKey, byte[] endKey) {
        ByteBuffer startKeyBuf = Util.byte2buff(startKey);
        ByteBuffer endKeyBuf = Util.byte2buff(endKey);
        KeyRange<ByteBuffer> range = new KeyRange<>(KeyRangeType.FORWARD_CLOSED, startKeyBuf, endKeyBuf);
        return new LmdbBucketIterator(db.iterate(unwrap(txn), range));
    }

    public long numEntries() {
        try (Transaction txn = env.startReadTxn()) {
            return db.stat(unwrap(txn)).entries;
        }
    }

    private ByteBuffer getKeyBuffer() {
        ByteBuffer keyBuf = keyBuffer.get();
        if (keyBuf == null) {
            keyBuf = ByteBuffer.allocateDirect(env.getMaxKeySize());
            keyBuffer.set(keyBuf);
        }
        return keyBuf;
    }

    private Txn<ByteBuffer> unwrap(Transaction txn) {
        return ((LmdbTransaction) txn).raw();
    }
}
