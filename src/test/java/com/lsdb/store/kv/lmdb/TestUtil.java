package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Bucket;
import com.lsdb.store.kv.Environment;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class TestUtil {

    public static String ENV_NAME = "test-env";
    public static int ENV_SIZE = 1024 * 1024 * 8;
    public static int ENV_MAX_BUCKETS = 32;

    public static Environment openTestEnv() {
        return LmdbEnvironment.open(ENV_NAME, ENV_SIZE, ENV_MAX_BUCKETS);
    }

    public static Bucket openTestBucket() {
        Environment env = openTestEnv();
        return env.openBucket("test-bucket");
    }

    public static byte[] str2bytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static String bytes2str(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void removeTestFiles() throws java.io.IOException {
        FileUtils.deleteDirectory(new File(LmdbEnvironment.DB_ROOT + ENV_NAME));
    }

}
