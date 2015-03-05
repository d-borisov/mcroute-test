package com.gc.app;


import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class Settings {

    private static final long DEFAULT_INTERVAL_LOG4J_CONF_UPDATE = 1000L;

    private final String memcachedHost;
    private final int memcachedPort;
    private final int expire;

    private final String log4jXml;
    private final boolean log4jTrack;
    private final long log4jDelay;

    private final int writeInterval;
    private final int startFrom;
    private final int steps;
    private final String regime;
    private final int readWriteInterval;

    public Settings(Ini iniFile) {
        this.memcachedHost = getOrDefault(iniFile, String.class, "memcached", "host", "127.0.0.1");
        this.memcachedPort = getOrDefault(iniFile, Integer.class, "memcached", "port", 11211);
        this.expire = getOrDefault(iniFile, Integer.class, "memcached", "expire", 3600);

        this.log4jXml = getOrDefault(iniFile, String.class, "logger", "log4j_xml", "log4j.xml");
        this.log4jTrack = getOrDefault(iniFile, Boolean.class, "logger", "track", false);
        final Long delay = getOrDefault(iniFile, Long.class, "logger", "delay", DEFAULT_INTERVAL_LOG4J_CONF_UPDATE);
        this.log4jDelay = delay > DEFAULT_INTERVAL_LOG4J_CONF_UPDATE ? delay : DEFAULT_INTERVAL_LOG4J_CONF_UPDATE;

        this.writeInterval = getOrDefault(iniFile, Integer.class, "settings", "write_interval", 1000);
        this.startFrom = getOrDefault(iniFile, Integer.class, "settings", "start_from", 1);
        this.steps = getOrDefault(iniFile, Integer.class, "settings", "steps", 1);
        this.regime = getOrDefault(iniFile, String.class, "settings", "regime", "write");
        this.readWriteInterval = getOrDefault(iniFile, Integer.class, "settings", "read_write_interval", 10);
    }

    public static Settings fromFile(String[] programArgs) throws CantStartException {
        final String fileName = programArgs.length >= 1 ? programArgs[0] : "settings.ini";

        final File file = new File(fileName);
        if (!file.exists()) {
            throw new CantStartException("settings file '%s' not found (%s)", fileName, file.getAbsolutePath());
        }

        try {
            return new Settings(new Ini(file));
        } catch (IOException e) {
            throw new CantStartException(e, "error while reading settings file '%s'", fileName);
        }
    }

    private static <T> T getOrDefault(Ini ini, Class<T> cls, String section, String option, T defaultValue) {
        try {
            final T value = ini.get(section, option, cls);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String getMemcachedHost() {
        return memcachedHost;
    }

    public int getMemcachedPort() {
        return memcachedPort;
    }

    public String getLog4jXml() {
        return log4jXml;
    }

    public boolean isLog4jTrack() {
        return log4jTrack;
    }

    public long getLog4jDelay() {
        return log4jDelay;
    }

    public int getWriteInterval() {
        return writeInterval;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public int getSteps() {
        return steps;
    }

    public int getExpire() {
        return expire;
    }

    public String getRegime() {
        return regime;
    }

    public int getReadWriteInterval() {
        return readWriteInterval;
    }
}
