package asg.games.yipee.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    private static Logger getLogger() {
        // Dynamically get the logger for the calling class
        Class<?> callerClass = stackWalker.getCallerClass();
        return LoggerFactory.getLogger(callerClass);
    }

    public static void trace(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isTraceEnabled()) {
            if (args == null || args.length == 0) {
                logger.trace(message);
            } else {
                logger.trace(message, args);
            }
        }
    }

    public static void debug(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isDebugEnabled()) {
            if (args == null || args.length == 0) {
                logger.debug(message);
            } else {
                logger.debug(message, args);
            }
        }
    }

    public static void info(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isInfoEnabled()) {
            if (args == null || args.length == 0) {
                logger.info(message);
            } else {
                logger.info(message, args);
            }
        }
    }

    public static void warn(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isWarnEnabled()) {
            if (args == null || args.length == 0) {
                logger.warn(message);
            } else {
                logger.warn(message, args);
            }
        }
    }

    public static void error(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isErrorEnabled()) {
            if (args == null || args.length == 0) {
                logger.error(message);
            } else {
                logger.error(message, args);
            }
        }
    }
}