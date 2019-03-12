package com.simplejcode.commons.misc.helpers;

public class UnrecoverableExceptionWrapper extends RuntimeException {

    public UnrecoverableExceptionWrapper(Throwable cause) {
        super(cause);
    }

}
