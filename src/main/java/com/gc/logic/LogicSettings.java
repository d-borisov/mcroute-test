package com.gc.logic;

import com.gc.app.Settings;

public class LogicSettings {

    public final Regime regime;
    public final int writeInterval;
    public final int startFrom;
    public final int steps;
    public final int expire;
    public final int readWriteInterval;

    public LogicSettings(Settings settings) {
        regime = Regime.fromStr(settings.getRegime());
        writeInterval = settings.getWriteInterval();
        startFrom = settings.getStartFrom();
        steps = settings.getSteps();
        expire = settings.getExpire();
        readWriteInterval = settings.getReadWriteInterval();
    }

    @Override
    public String toString() {
        return "LogicSettings{" +
                "regime=" + regime +
                ", writeInterval=" + writeInterval +
                ", startFrom=" + startFrom +
                ", steps=" + steps +
                ", expire=" + expire +
                ", readWriteInterval=" + readWriteInterval +
                '}';
    }
}
