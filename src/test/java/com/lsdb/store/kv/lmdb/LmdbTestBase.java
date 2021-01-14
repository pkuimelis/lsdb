package com.lsdb.store.kv.lmdb;

import org.junit.After;

public class LmdbTestBase {

    @After
    public void tearDown() throws java.io.IOException {
        TestUtil.removeTestFiles();
    }
}
