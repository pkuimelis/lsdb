package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Bucket;
import com.lsdb.store.kv.Environment;
import com.lsdb.store.kv.Transaction;
import org.lmdbjava.Env;

import java.io.File;
import java.nio.ByteBuffer;

import static org.lmdbjava.DbiFlags.MDB_CREATE;

/**
 * Implementation of the Environment interface using the LMDB-Java environment API.
 */
public class LmdbEnvironment implements Environment {

    public static String DB_ROOT = "/home/hdd-1/lsdb/";

    private final Env<ByteBuffer> env;

    private LmdbEnvironment(Env<ByteBuffer> env) {
        this.env = env;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Environment open(String envName, int size, int maxBuckets) {
        File path = new File(DB_ROOT + envName);
        path.mkdirs();
        return new LmdbEnvironment(Env.create()
                .setMaxDbs(maxBuckets)
                .setMapSize(size)
                .open(path));
    }

    @Override
    public Bucket openBucket(String bucketName) {
        return new LmdbBucket(this, env.openDbi(bucketName, MDB_CREATE));
    }

    @Override
    public Transaction startReadTxn() {
        return new LmdbTransaction(env.txnRead());
    }

    @Override
    public Transaction startWriteTxn() {
        return new LmdbTransaction(env.txnWrite());
    }

    @Override
    public int getMaxKeySize() {
        return env.getMaxKeySize();
    }

    @Override
    public void close() {
        env.close();
    }
}
