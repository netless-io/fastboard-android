package io.agora.board.fast;

import android.util.Log;

public class FastLogger {
    private static Logger logger = new DefaultLogger();

    static void debug(String msg) {
        logger.debug(msg);
    }

    static void info(String msg) {
        logger.info(msg);
    }

    static void warn(String msg) {
        logger.warn(msg);
    }

    static void error(String msg) {
        logger.error(msg);
    }

    static void error(String msg, Throwable throwable) {
        logger.error(msg, throwable);
    }

    public static void setLogger(Logger logger) {
        if (logger != null) {
            FastLogger.logger = logger;
        }
    }

    public interface Logger {
        void debug(String msg);

        void info(String msg);

        void warn(String msg);

        void error(String msg);

        void error(String msg, Throwable throwable);
    }

    private static class DefaultLogger implements Logger {
        private final static String LOG_TAG = "Fastboard";

        @Override
        public void debug(String msg) {
            Log.d(LOG_TAG, msg);
        }

        @Override
        public void info(String msg) {
            Log.i(LOG_TAG, msg);
        }

        @Override
        public void warn(String msg) {
            Log.w(LOG_TAG, msg);
        }

        @Override
        public void error(String msg) {
            error(msg, null);
        }

        @Override
        public void error(String msg, Throwable throwable) {
            Log.e(LOG_TAG, msg, throwable);
        }
    }
}
