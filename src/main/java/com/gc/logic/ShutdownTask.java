package com.gc.logic;

import com.gc.log.Logger;

public class ShutdownTask implements Runnable {

    private static final Logger logger = Logger.newLogger(ShutdownTask.class);
    private final WorkResult.Builder workResultBuilder;

    public ShutdownTask(WorkResult.Builder workResultBuilder) {
        this.workResultBuilder = workResultBuilder;
    }

    @Override
    public void run() {
        logger.debug("ShutdownTask started");
        boolean alreadyConstructed = workResultBuilder.isAlreadyConstructed();
        if (!alreadyConstructed) {
            WorkResultWriter.write(workResultBuilder.build());
        }
        logger.debug("ShutdownTask done. isResultWrittenByShutdown: %s", !alreadyConstructed);
    }

}