package com.gc.logic;

import com.gc.log.Logger;
import com.gc.memcached.Memcached;

public class Logic {

    private static final Logger logger = Logger.newLogger(Logic.class);

    public static void doLogic(WorkResult.Builder workResultBuilder, Memcached memcached, LogicSettings sets) {
        final int expire = sets.expire;
        final int steps = sets.steps;
        int key = sets.startFrom;

        switch (sets.regime) {
            case WRITE:
                for (int i = 0; i < steps; i++) {
                    write(workResultBuilder, memcached, key, expire);
                    sleep(sets.writeInterval);

                    key++;
                }
                break;

            case READ:
                for (int i = 0; i < steps; i++) {
                    read(workResultBuilder, memcached, key);
                    key++;
                }
                break;

            case ALL_WRITE_ALL_READ:
                for (int i = 0; i < steps; i++) {
                    write(workResultBuilder, memcached, key, expire);
                    sleep(sets.writeInterval);

                    key++;
                }

                key = sets.startFrom;
                sleep(sets.readWriteInterval);

                for (int i = 0; i < steps; i++) {
                    read(workResultBuilder, memcached, key);
                    key++;
                }

                break;

            case ONE_WRITE_ONE_READ:
                for (int i = 0; i < steps; i++) {
                    write(workResultBuilder, memcached, key, expire);

                    sleep(sets.readWriteInterval);
                    read(workResultBuilder, memcached, key);

                    sleep(sets.writeInterval);
                    key++;
                }

                break;
        }
    }

    private static void write(WorkResult.Builder builder, Memcached memcached, int key, int expire) {
        logger.info("Try to write key: " + key);

        final boolean write = memcached.write(key, expire);
        if (write) {
            builder.written(key);
        } else {
            builder.notWritten(key);
        }
    }

    private static void read(WorkResult.Builder builder, Memcached memcached, int key) {
        logger.info("Try to read key: " + key);

        final Memcached.ReadResult read = memcached.read(key);
        switch (read) {
            case NOT_READ:
                builder.notRead(key);
                break;

            case READ_NOT_SAME:
                builder.readNotSame(key);
                break;

            case READ_SAME:
                builder.readSame(key);
                break;

        }
    }

    private static void sleep(int interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            logger.warning(e, "Exception while sleeping");
        }
    }
}
