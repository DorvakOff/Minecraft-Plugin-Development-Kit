package com.dorvak.mpdk.utils;

import java.util.logging.Logger;

public class MPDKLogger {

    public static void info(String message, Object... args) {
        getLoggerForCallingClass().info(String.format(message, args));
    }

    public static void warning(String message, Object... args) {
        getLoggerForCallingClass().warning(String.format(message, args));
    }

    public static void severe(String message, Object... args) {
        getLoggerForCallingClass().severe(String.format(message, args));
    }

    public static Logger getLoggerForCallingClass() {
        Class<?> caller = Thread.currentThread().getStackTrace()[3].getClass();
        return Logger.getLogger(caller.getName());
    }
}
