package com.simplejcode.commons.misc.structures;

import com.simplejcode.commons.misc.ExceptionUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Faster replacement for java.util.logging.Logger
 */
public class FastLogger {

    public static final String DATE_PATTERN = "$date";

    public enum Level {

        FINEST(1, "Finest", "----FINEST   "),
        FINER(2, "Finer", "----FINER    "),
        FINE(3, "Fine", "----FINE     "),
        CONFIG(4, "Config", "----CONFIG   "),
        INFO(5, "Info", "----INFO     "),
        WARNING(6, "Warning", "----WARNING  "),
        SEVERE(7, "Severe", "----SEVERE   "),
        CRITICAL(8, "Critical", "----CRITICAL ");

        public final int value;
        public final String name, prefix;

        Level(int value, String name, String prefix) {
            this.value = value;
            this.name = name;
            this.prefix = prefix;
        }
    }

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("[dd.MM.yy HH:mm:ss] ");

    protected int levelValue;
    protected boolean logDate = true;
    protected OutputStream source;
    protected PrintWriter out;
    protected String fileName = "";
    protected File outputFile;

    public FastLogger(int levelValue, boolean logDate) {
        setLevelValue(levelValue);
        setLogDate(logDate);
    }

    public synchronized void setLevelValue(int levelValue) {
        this.levelValue = levelValue;
    }

    public synchronized void setLogDate(boolean logDate) {
        this.logDate = logDate;
    }

    public synchronized void setOutput(OutputStream outputStream) {
        if (out != null) {
            out.flush();
            if (source != System.out) {
                close();
            }
        }
        out = new PrintWriter(source = outputStream, true);
    }

    protected void trySetFileOutput(File file) {
        try {
            setOutput(new FileOutputStream(file, true));
        } catch (Exception e) {
            setOutput(System.out);
        }
    }

    public synchronized void setOutputBase(String file) {
        if (source == System.out && (file == null || file.isEmpty())) {
            return;
        }
        if (file == null || file.isEmpty()) {
            fileName = null;
            outputFile = null;
            setOutput(System.out);
            return;
        }
        String newName = file.replace(DATE_PATTERN, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (!newName.equals(fileName)) {
            trySetFileOutput(outputFile = new File(fileName = newName));
        }
    }

    public synchronized void close() {
        try {
            out.close();
        } catch (Exception ignore) {
        }
    }

    public void log(String x, Level level) {
        if (out == null || level.value < this.levelValue) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(level.prefix);
        if (logDate) {
            sb.append(SIMPLE_DATE_FORMAT.format(new Date()));
        }
        sb.append(x);
        if (outputFile != null && !outputFile.exists()) {
            trySetFileOutput(outputFile);
        }
        out.println(sb.toString());
    }


    public void log(Object x) {
        log(x.toString());
    }

    public void log(Object x, Level level) {
        log(x.toString(), level);
    }

    public void log(Throwable e) {
        log(e, Level.SEVERE);
    }

    public void log(Throwable e, Level level) {
        log(ExceptionUtils.stringifyException(e), level);
    }

    public void log(String x) {
        log(x, Level.INFO);
    }

}
