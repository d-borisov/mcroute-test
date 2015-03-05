package com.gc;

import com.gc.app.CantStartException;
import com.gc.app.Settings;
import com.gc.log.Logger;
import com.gc.logic.*;
import com.gc.memcached.Memcached;
import com.gc.memcached.MemcachedProvider;
import com.gc.utils.Dates;

import java.util.Locale;

import static com.google.common.base.Throwables.getStackTraceAsString;

public class Main {

    public static void main(String[] args) throws Exception {
        Locale.setDefault(new Locale("en", "US"));

        printHeader();
        try {
            final Settings settings = Settings.fromFile(args);
            final LogicSettings logicSettings = new LogicSettings(settings);

            Logger.init(settings);
            Logger logger = Logger.newLogger("APPLICATION STARTING");
            logger.emptyLine();
            logger.info("Will work with settings: %s", logicSettings);
            logger.emptyLine();

            final WorkResult.Builder workResultBuilder = WorkResult.builder(logicSettings);
            final ShutdownTask shutdownTask = new ShutdownTask(workResultBuilder);
            Runtime.getRuntime().addShutdownHook(new Thread(shutdownTask));

            try (final Memcached memcached = MemcachedProvider.getMemcached(settings)) {
                Logic.doLogic(workResultBuilder, memcached, logicSettings);
            }
            WorkResultWriter.write(workResultBuilder.build());
        } catch (CantStartException e) {
            System.out.println();
            System.out.println(getStackTraceAsString(e));
            printStartingResult("server does not started");

            System.exit(-1);
        }
    }

    private static void printHeader() {
        System.out.println("*********************");
        System.out.println("* MCROUTER TEST APP *");
        System.out.println("*********************");
    }

    private static void printStartingResult(String result) {
        System.out.println();
        System.out.println("MCROUTER TEST APP STARTING RESULT [" + Dates.nowDateTime() + "]: " + result);
        System.out.println();
    }
}
