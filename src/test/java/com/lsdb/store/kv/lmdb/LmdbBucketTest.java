package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Bucket;
import com.lsdb.store.kv.Environment;
import com.lsdb.store.kv.Transaction;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

public class LmdbBucketTest extends LmdbTestBase {

    @Test
    public void basic() {

        Bucket bucket = TestUtil.openTestBucket();

        bucket.put(TestUtil.str2bytes("key0"), TestUtil.str2bytes("hello world"));
        byte[] val0 = bucket.get(TestUtil.str2bytes(("key0")));

        Assert.assertEquals("hello world", TestUtil.bytes2str(val0));
        System.out.println("value [" + TestUtil.bytes2str(val0) + "] was fetched from the store.");

        bucket.put(TestUtil.str2bytes("key0"), TestUtil.str2bytes("hello again"));
        val0 = bucket.get(TestUtil.str2bytes(("key0")));

        Assert.assertEquals("hello again", TestUtil.bytes2str(val0));
        System.out.println("value [" + TestUtil.bytes2str(val0) + "] was fetched from the store.");

        bucket.put(TestUtil.str2bytes("key1"), TestUtil.str2bytes("what's up"));
        byte[] val1 = bucket.get(TestUtil.str2bytes(("key1")));

        Assert.assertEquals("what's up", TestUtil.bytes2str(val1));
        System.out.println("value [" + TestUtil.bytes2str(val1) + "] was fetched from the store.");
    }

    @Test
    public void testStats() {

        Bucket bucket = TestUtil.openTestBucket();

        bucket.put(TestUtil.str2bytes("key0"), TestUtil.str2bytes("val0"));
        bucket.put(TestUtil.str2bytes("key1"), TestUtil.str2bytes("val1"));
        bucket.put(TestUtil.str2bytes("key2"), TestUtil.str2bytes("val2"));
        bucket.put(TestUtil.str2bytes("key0"), TestUtil.str2bytes("val420"));

        Assert.assertEquals(3, ((LmdbBucket) bucket).numEntries());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testReadTxn() throws InterruptedException {

        Environment env = TestUtil.openTestEnv();
        Bucket bucket = env.openBucket("bucket-0");
        bucket.put(TestUtil.str2bytes("key0"), TestUtil.str2bytes("hello world"));
        byte[] val0;

        // Start a read transaction @rtx.
        try (Transaction rtx = env.startReadTxn()) {

            // Update the value for key0 in another thread; this should not be visible within @rtx.
            ExecutorService es = newCachedThreadPool();
            es.execute(() -> {
                bucket.put(TestUtil.str2bytes("key0"), TestUtil.str2bytes("yeet"));
            });
            es.shutdown();
            es.awaitTermination(10, SECONDS);

            val0 = bucket.get(rtx, TestUtil.str2bytes(("key0")));
            Assert.assertEquals("hello world", TestUtil.bytes2str(val0));
        }

        // Now that we have exited @rtx, we should see the updated value for key0.
        val0 = bucket.get(TestUtil.str2bytes(("key0")));
        Assert.assertEquals("yeet", TestUtil.bytes2str(val0));
    }

    @Test
    public void testReadWriteTxn() {

        Environment env = TestUtil.openTestEnv();
        Bucket bucket = env.openBucket("bucket-0");
        byte[] val0;

        // Start a write transaction @txn.
        try (Transaction txn = env.startWriteTxn()) {

            // Add a record; do not commit.
            bucket.put(txn, TestUtil.str2bytes("key0"), TestUtil.str2bytes("hello world"));
            val0 = bucket.get(txn, TestUtil.str2bytes(("key0")));
            Assert.assertEquals("hello world", TestUtil.bytes2str(val0));
        }

        // The record could not be found because the op was rolled back when the transaction was closed.
        val0 = bucket.get(TestUtil.str2bytes(("key0")));
        Assert.assertNull(val0);

        // This time, actually commit the transaction.
        try (Transaction txn = env.startWriteTxn()) {

            bucket.put(txn, TestUtil.str2bytes("key0"), TestUtil.str2bytes("hello world"));
            val0 = bucket.get(txn, TestUtil.str2bytes(("key0")));
            Assert.assertEquals("hello world", TestUtil.bytes2str(val0));
            txn.commit();
        }

        val0 = bucket.get(TestUtil.str2bytes(("key0")));
        Assert.assertEquals("hello world", TestUtil.bytes2str(val0));
    }
}