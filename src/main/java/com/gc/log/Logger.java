package com.gc.log;

import com.gc.app.CantStartException;
import com.gc.app.Settings;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;

import static java.lang.String.format;

public class Logger {
    private static final ImmutableMap<LogLevel, Level> log4jLevels = ImmutableMap.<LogLevel, Level>builder()
            .put(LogLevel.ALL, Level.ALL)
            .put(LogLevel.INFO, Level.INFO)
            .put(LogLevel.DEBUG, Level.DEBUG)
            .build();

    private final org.apache.log4j.Logger innerLogger;

    Logger(org.apache.log4j.Logger logger) {
        this.innerLogger = logger;
    }

    public static Logger newLogger(Class cls) {
        return new Logger(org.apache.log4j.Logger.getLogger(cls));
    }

    public static Logger newLogger(String name) {
        return new Logger(org.apache.log4j.Logger.getLogger(name));
    }

    public static synchronized void setLevel(LogLevel newLevel) {
        final Level level = log4jLevels.get(newLevel);
        LogManager.getRootLogger().setLevel(level != null ? level : Level.INFO);
    }

    public static void init(Settings settings) throws CantStartException {
        final File log4jXml;
        try {
            log4jXml = new File(settings.getLog4jXml());
        } catch (Exception e) {
            throw new CantStartException("Cant target Logger configuration file");
        }
        if (!log4jXml.exists()) {
            throw new CantStartException("Logger configuration file not found");
        }
        if (settings.isLog4jTrack()) {
            DOMConfigurator.configureAndWatch(log4jXml.getAbsolutePath(), settings.getLog4jDelay());
        } else {
            DOMConfigurator.configure(log4jXml.getAbsolutePath());
        }
    }

    public void emptyLine() {
        info("");
    }

    public boolean isDebugEnabled() {
        return innerLogger.isDebugEnabled();
    }

    public boolean isWarningEnabled() {
        return innerLogger.isEnabledFor(Level.WARN);
    }

    public void info(String message) {
        innerLogger.info(message);
    }

    public void info(String pattern, Object... params) {
        info(format(pattern, params));
    }

    public void debug(String message) {
        innerLogger.debug(message);
    }

    public void debug(String message, Object... params) {
        if (isDebugEnabled()) {
            innerLogger.debug(String.format(message, params));
        }
    }

    public void debug(Throwable t, String message, Object... params) {
        if (isDebugEnabled()) {
            innerLogger.debug(String.format(message, params), t);
        }
    }

    public void warning(String message) {
        innerLogger.warn(message);
    }

    public void warning(String message, Object... params) {
        if (isWarningEnabled()) {
            innerLogger.warn(String.format(message, params));
        }
    }

    public void warning(Throwable t, String message, Object... params) {
        if (isWarningEnabled()) {
            innerLogger.warn(String.format(message, params), t);
        }
    }

}
