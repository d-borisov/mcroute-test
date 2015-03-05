package com.gc.logic;

import java.util.ArrayList;
import java.util.List;

public class WorkResult {
    public final int from;
    public final int to;
    public final int steps;
    public final boolean writeStarted;
    public final boolean readStarted;
    public final List<Integer> notWritten;
    public final List<Integer> written;
    public final List<Integer> notRead;
    public final List<Integer> readNotSame;
    public final List<Integer> readSame;

    public WorkResult(
            int from,
            int to,
            int steps,
            boolean writeStarted,
            boolean readStarted,
            List<Integer> notWritten,
            List<Integer> written,
            List<Integer> notRead,
            List<Integer> readNotSame,
            List<Integer> readSame
    ) {
        this.from = from;
        this.to = to;
        this.steps = steps;
        this.writeStarted = writeStarted;
        this.readStarted = readStarted;
        this.notWritten = notWritten;
        this.written = written;
        this.notRead = notRead;
        this.readNotSame = readNotSame;
        this.readSame = readSame;
    }

    public static Builder builder(LogicSettings logicSettings) {
        final int from = logicSettings.startFrom;
        final int to = from + logicSettings.steps - 1;
        return new Builder(from, to, logicSettings.steps);
    }

    public static class Builder {
        private boolean isAlreadyConstructed = false;
        private final int from;
        private final int to;
        private final int steps;
        private boolean writeStarted = false;
        private boolean readStarted = false;
        private final List<Integer> notWritten = new ArrayList<>();
        private final List<Integer> written = new ArrayList<>();
        private final List<Integer> notRead = new ArrayList<>();
        private final List<Integer> readSame = new ArrayList<>();
        private final List<Integer> readNotSame = new ArrayList<>();

        private Builder(int from, int to, int steps) {
            this.from = from;
            this.to = to;
            this.steps = steps;
        }

        public boolean isAlreadyConstructed() {
            return isAlreadyConstructed;
        }

        public WorkResult build() {
            isAlreadyConstructed = true;
            return new WorkResult(
                    from,
                    to,
                    steps,
                    writeStarted,
                    readStarted,
                    notWritten,
                    written,
                    notRead,
                    readNotSame,
                    readSame
            );
        }

        public Builder notWritten(int key) {
            writeStarted = true;
            notWritten.add(key);
            return this;
        }

        public Builder written(int key) {
            writeStarted = true;
            written.add(key);
            return this;
        }

        public Builder notRead(int key) {
            readStarted = true;
            notRead.add(key);
            return this;
        }

        public Builder readNotSame(int key) {
            readStarted = true;
            readNotSame.add(key);
            return this;
        }

        public Builder readSame(int key) {
            readStarted = true;
            readSame.add(key);
            return this;
        }
    }
}
