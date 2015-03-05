package com.gc.app;

import static java.lang.String.format;

public class CantStartException extends Exception {

    public CantStartException(String message) {
        super(message);
    }

    public CantStartException(String pattern, Object... params) {
        this(format(pattern, params));
    }

    public CantStartException(Throwable cause, String pattern, Object... params) {
        super(format(pattern, params), cause);
    }

}
