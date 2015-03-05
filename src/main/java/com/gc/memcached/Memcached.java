package com.gc.memcached;

public interface Memcached extends AutoCloseable {

    boolean write(int iKey, int expire);

    ReadResult read(int iKey);

    public static enum ReadResult {
        NOT_READ,
        READ_NOT_SAME,
        READ_SAME
    }
}
