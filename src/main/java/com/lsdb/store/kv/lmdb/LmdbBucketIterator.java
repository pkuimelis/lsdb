package com.lsdb.store.kv.lmdb;

import com.lsdb.store.kv.Util.BytePair;

import org.lmdbjava.CursorIterable;

import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * Wraps LMDB-Java CursorIterable.
 */
public class LmdbBucketIterator implements Iterable<BytePair>, AutoCloseable {

    public CursorIterable<ByteBuffer> cursor;

    public LmdbBucketIterator(CursorIterable<ByteBuffer> cursor) {
        this.cursor = cursor;
    }

    @Override
    public Iterator<BytePair> iterator() {

        Iterator<CursorIterable.KeyVal<ByteBuffer>> iterator = cursor.iterator();
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public BytePair next() {
                CursorIterable.KeyVal<ByteBuffer> next = iterator.next();
                ByteBuffer keyBuf = next.key();
                ByteBuffer valBuf = next.val();
                byte[] key = Util.buff2byte(keyBuf);
                byte[] val = Util.buff2byte(valBuf);
                return new BytePair(key, val);
            }
        };
    }

    @Override
    public void close() {
        cursor.close();
    }
}
