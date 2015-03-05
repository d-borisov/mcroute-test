package com.gc.logic;

import com.google.common.base.Strings;

public enum Regime {
    WRITE("write"),
    READ("read"),
    ALL_WRITE_ALL_READ("all_write_all_read"),
    ONE_WRITE_ONE_READ("one_write_one_read");

    private final String str;

    Regime(String str) {
        this.str = str;
    }


    public static Regime fromStr(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return WRITE;
        }
        for (Regime regime : Regime.values()) {
            if (str.equals(regime.str)) {
                return regime;
            }
        }
        return WRITE;
    }
}
