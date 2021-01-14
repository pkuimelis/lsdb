package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Bucket;
import com.lsdb.store.kv.Environment;
import org.junit.Test;

public class LmdbEnvironmentTest extends LmdbTestBase {

    @Test
    public void testOpen() {
        Environment env = TestUtil.openTestEnv();
    }

    @Test
    public void testOpenBucket() {
        Bucket bucket = TestUtil.openTestBucket();
    }

    @Test
    public void oneEnvMultipleBuckets() {
        Environment env = TestUtil.openTestEnv();
        Bucket bucket0 = env.openBucket("bucket-0");
        Bucket bucket1 = env.openBucket("bucket-1");
        Bucket bucket3 = env.openBucket("bucket-2");
    }
}
