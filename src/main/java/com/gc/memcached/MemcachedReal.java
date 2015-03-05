package com.gc.memcached;

import com.gc.log.Logger;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MemcachedReal implements Memcached {

    private static final Logger logger = Logger.newLogger(MemcachedReal.class);

    private static final int DURATION_IN_SEC = 5;
    private final MemcachedClient client;

    public MemcachedReal(MemcachedClient client) {
        this.client = client;
    }

    @Override
    public void close() {
        try {
            client.shutdown();
        } catch (Exception e) {
            logger.warning(e, "Memcached stop() exception");
        }
    }

    @Override
    public boolean write(int iKey, int expire) {
        final String key = "key_" + iKey;
        final String value = "value_" + iKey;

        try {
            final long before = System.currentTimeMillis();

            final OperationFuture<Boolean> set = client.set(key, expire, value);
            final Boolean resultOfGet = set.get(DURATION_IN_SEC, TimeUnit.SECONDS);

            final long after = System.currentTimeMillis();
            logger.debug("WRITE iKey: " + iKey + ", expire: " + expire + ", time: " + (after - before) + ", resultOfGet: " + resultOfGet + ", done: " + set.isDone());

            return set.isDone();
        } catch (Exception e) {
            logger.warning(e, "Memcached WRITE exception for iKey: " + iKey + ", expire: " + expire);
            return false;
        }
    }

    @Override
    public ReadResult read(int iKey) {
        final String key = "key_" + iKey;
        final String val = "value_" + iKey;

        try {
            final long before = System.currentTimeMillis();

            final Object value;
            final GetFuture<Object> future = client.asyncGet(key);
            try {
                value = future.get(DURATION_IN_SEC, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                future.cancel(false);

                logger.warning(e, "Memcached read() TimeoutException");
                return ReadResult.NOT_READ;
            }

            final long after = System.currentTimeMillis();
            logger.debug("READ iKey: %s, want: %s, get: %s, time: %d", iKey, val, value, (after - before));

            if (value == null) {
                return ReadResult.NOT_READ;
            }

            return value.equals("" + value)
                    ? ReadResult.READ_SAME
                    : ReadResult.READ_NOT_SAME;

        } catch (Exception e) {
            logger.warning(e, "Memcached READ exception for iKey: " + iKey);
            return ReadResult.NOT_READ;
        }
    }
}
