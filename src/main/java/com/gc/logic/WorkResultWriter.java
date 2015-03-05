package com.gc.logic;

import com.gc.log.Logger;
import com.gc.utils.Dates;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class WorkResultWriter {

    private static final Logger logger = Logger.newLogger(WorkResultWriter.class);

    public static void write(WorkResult workResult) {
        for (String s : constructReport(workResult)) {
            logger.info(s);
        }
    }

    // package private для тестов
    static List<String> constructReport(WorkResult workResult) {
        final List<String> result = new ArrayList<>();

        result.add("--------- Время " + Dates.nowDateTime());
        result.add(format("--------- От %d До %d Кол-во шагов %d", workResult.from, workResult.to, workResult.steps));

        result.add("--- Режим WRITE");
        if (workResult.writeStarted) {
            result.add(format("Не записалось (%d штук): %s", workResult.notWritten.size(), workResult.notWritten));
            result.add(format("Записалось (%d штук): %s", workResult.written.size(), workResult.written));
        } else {
            result.add("не выполнялся");
        }

        result.add("--- Режим READ");
        if (workResult.readStarted) {
            result.add(format("Не прочиталось (%d штук): %s", workResult.notRead.size(), workResult.notRead));
            result.add(format("Прочиталось не верно (%d штук): %s", workResult.readNotSame.size(), workResult.readNotSame));
            result.add(format("Прочиталось (%d штук): %s", workResult.readSame.size(), workResult.readSame));
        } else {
            result.add("не выполнялся");
        }
        result.add("");
        return result;
    }
}
