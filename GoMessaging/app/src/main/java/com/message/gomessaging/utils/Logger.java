package com.message.gomessaging.utils;

import android.util.Log;

/**
 * Prints the log message in the LogCat.
 */
public class Logger {

    private static boolean DEBUG = true;
    private static final String LOG_TAG = "com.message.gomessaging";

    public static void d(String message) {
        if (DEBUG) {
            Log.d(LOG_TAG, "" + message);
        }
    }

    public static void d(String logtag, String message) {
        if (DEBUG) {
            Log.d(logtag, "" + message);
        }
    }

    public static void i(String message) {
        if (DEBUG) {
            Log.i(LOG_TAG, "" + message);
        }
    }

    public static void i(String logtag, String message) {
        if (DEBUG) {
            Log.i(logtag, "" + message);
        }
    }

    public static void w(String message) {
        if (DEBUG) {
            Log.w(LOG_TAG, "" + message);
        }
    }

    public static void w(String logtag, String message) {
        if (DEBUG) {
            Log.w(logtag, "" + message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Log.e(LOG_TAG, "" + message);
        }
    }

    public static void e(String logtag, String message) {
        if (DEBUG) {
            Log.e(logtag, "" + message);
        }
    }

    /**
     * Disables the Logger to print LOGs
     */
    public static void enableLogging(boolean flag) {
        Logger.DEBUG = flag;
    }

    /**
     * Returns whether Logging is enabled or not
     */
    public static boolean isLoggingEnabled() {
        return DEBUG;
    }
}