package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Util.BytePair;

import com.lsdb.store.kv.Bucket;
import com.lsdb.store.kv.Environment;
import com.lsdb.store.kv.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LmdbBucketIteratorTest extends LmdbTestBase {

    @Test
    public void basic() {

        Environment env = TestUtil.openTestEnv();
        Bucket bucket = env.openBucket("bucket-0");

        bucket.put(TestUtil.str2bytes("2"), TestUtil.str2bytes("foo"));
        bucket.put(TestUtil.str2bytes("4"), TestUtil.str2bytes("foo"));
        bucket.put(TestUtil.str2bytes("6"), TestUtil.str2bytes("foo"));
        bucket.put(TestUtil.str2bytes("8"), TestUtil.str2bytes("foo"));

        // keys: [2, 4, 6, 8]
        // query: iterate all
        // expect: [2, 4, 6, 8]

        List<String> keysIterateAll = new ArrayList<>();

        try (Transaction txn = env.startReadTxn()) {
            for (BytePair pair : bucket.iterateAll(txn)) {
                keysIterateAll.add(TestUtil.bytes2str(pair.key));
            }
        }

        Assert.assertEquals(Arrays.asList("2", "4", "6", "8"), keysIterateAll);

        // keys: [2, 4, 6, 8]
        // query: iterate [3, 7]
        // expect: [4, 6]

        List<String> keysIterateRange0 = new ArrayList<>();

        try (Transaction txn = env.startReadTxn()) {
            for (BytePair pair : bucket.iterateRange(txn, TestUtil.str2bytes("3"), TestUtil.str2bytes("7"))) {
                keysIterateRange0.add(TestUtil.bytes2str(pair.key));
            }
        }

        Assert.assertEquals(Arrays.asList("4", "6"), keysIterateRange0);

        // keys: [2, 4, 6, 8]
        // query: iterate [2, 6]
        // expect: [2, 4, 6]

        List<String> keysIterateRange1 = new ArrayList<>();

        try (Transaction txn = env.startReadTxn()) {
            for (BytePair pair : bucket.iterateRange(txn, TestUtil.str2bytes("2"), TestUtil.str2bytes("6"))) {
                keysIterateRange1.add(TestUtil.bytes2str(pair.key));
            }
        }

        Assert.assertEquals(Arrays.asList("2", "4", "6"), keysIterateRange1);
    }
}