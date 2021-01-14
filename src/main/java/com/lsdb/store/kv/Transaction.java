package com.lsdb.store.kv;

/**
 * Interface for a transaction handle object that is Autocloseable.
 */
public interface Transaction extends AutoCloseable {

    public void commit();

    public void abort();

    @Override
    public void close();

}