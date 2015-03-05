package com.gc.memcached;

import com.gc.app.CantStartException;
import com.gc.app.Settings;
import com.gc.log.Logger;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

public class MemcachedProvider {

    private static final Logger logger = Logger.newLogger(MemcachedProvider.class);

    public static Memcached getMemcached(Settings settings) throws CantStartException {
        useLog4jLogger();

        final String host = settings.getMemcachedHost();
        final int port = settings.getMemcachedPort();

        logger.info("Memcached client try to start for server on host: " + host + " port: " + port);

        final MemcachedClient c;
        try {
            c = new MemcachedClient(new InetSocketAddress(host, port));
        } catch (IOException e) {
            throw new CantStartException(e, "Exception while creating memcached client");
        }
        return new MemcachedReal(c);
    }

    /*
     * Для логирования используем логгер Log4j - тот же, что и остальные классы приложения.
     * Код взят отсюда: http://code.google.com/p/spymemcached/wiki/Logging
     */
    private static void useLog4jLogger() {
        final Properties systemProperties = System.getProperties();
        systemProperties.put("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        System.setProperties(systemProperties);
    }

}
