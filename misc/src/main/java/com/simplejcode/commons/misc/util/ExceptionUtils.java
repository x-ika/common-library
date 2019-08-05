package com.simplejcode.commons.misc.util;

import javax.ejb.EJBException;
import java.io.*;
import java.lang.reflect.*;

public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static ExceptionWrapper wrap(Throwable throwable) {
        return new ExceptionWrapper(throwable);
    }

    public static RuntimeException generate(String message) {
        return new RuntimeException(message);
    }

    public static String stringifyException(Throwable e) {
        StringWriter stm = new StringWriter();
        PrintWriter wrt = new PrintWriter(stm);
        e.printStackTrace(wrt);
        wrt.close();
        return stm.toString();
    }

    public static Throwable retrieveCause(Throwable throwable) {
        return retrieveCause(throwable,
                UndeclaredThrowableException.class,
                InvocationTargetException.class,
                EJBException.class);
    }

    public static Throwable retrieveCauseIncludingCustoms(Throwable throwable) {
        return retrieveCause(throwable,
                UndeclaredThrowableException.class,
                InvocationTargetException.class,
                ExceptionWrapper.class);
    }

    public static Throwable retrieveCause(Throwable throwable, Class... boxes) {
        M:
        while (true) {
            for (Class box : boxes) {
                if (box.isInstance(throwable)) {
                    throwable = throwable.getCause();
                    continue M;
                }
            }
            return throwable;
        }
    }

}
