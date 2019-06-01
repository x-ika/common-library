package com.simplejcode.commons.misc;

import java.io.File;

public final class ProcessUtils {

    public static final int TIME_LIMIT_ERROR_CODE = -1;

    private ProcessUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static int execute(File exec, int waitTime, String... params) throws Exception {

        if (exec.getName().endsWith(".exe")) {
            String[] command = new String[params.length + 1];
            command[0] = exec.getAbsolutePath();
            System.arraycopy(params, 0, command, 1, params.length);
            return run(exec.getParentFile(), waitTime, command);
        }

        if (exec.getName().endsWith(".class")) {
            String[] command = new String[params.length + 5];
            command[0] = "java";
            command[1] = "-Xms256m";
            command[2] = "-Xmx256m";
            command[3] = "-Xss64m";
            command[4] = exec.getName().substring(0, exec.getName().length() - 6);
            System.arraycopy(params, 0, command, 5, params.length);
            return run(exec.getParentFile(), waitTime, command);
        }

        if (exec.getName().endsWith(".jar")) {
            String[] command = new String[params.length + 6];
            command[0] = "java";
            command[1] = "-jar";
            command[2] = "-Xms256m";
            command[3] = "-Xmx256m";
            command[4] = "-Xss64m";
            command[5] = exec.getName();
            System.arraycopy(params, 0, command, 6, params.length);
            return run(exec.getParentFile(), waitTime, command);
        }

        throw new RuntimeException("Unexpected executable file type: " + exec.getName());
    }

    public static int execute(String command, int waitTime) throws Exception {
        return run(Runtime.getRuntime().exec(command), waitTime);
    }

    private static int run(File directory, int waitTime, String... command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(directory);
        return run(processBuilder.start(), waitTime);
    }

    private static int run(Process process, int waitTime) throws InterruptedException {
        long endTime = System.nanoTime() + (long) 1e6 * waitTime;
        do {
            Thread.sleep(20);
            //noinspection EmptyCatchBlock
            try {
                return process.exitValue();
            } catch (IllegalThreadStateException e) {
            }
        } while (System.nanoTime() < endTime);
        process.destroy();
        return TIME_LIMIT_ERROR_CODE;
    }

}
