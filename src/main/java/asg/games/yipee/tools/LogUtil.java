package asg.games.yipee.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging messages dynamically with the correct logger for the calling class.
 * Uses SLF4J for logging and supports trace, debug, info, warn, and error levels.
 */
public class LogUtil {
    private static final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    // Prevent instantiation
    private LogUtil() {
    }

    /**
     * Retrieves the logger for the calling class dynamically using StackWalker.
     *
     * @return Logger instance for the calling class
     */
    private static Logger getLogger() {
        Class<?> callerClass = stackWalker.getCallerClass();
        return LoggerFactory.getLogger(callerClass);
    }

    public static void trace(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isTraceEnabled()) {
            logger.trace(message == null ? "null" : message, args);
        }
    }

    public static void debug(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug(message == null ? "null" : message, args);
        }
    }

    public static void info(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isInfoEnabled()) {
            logger.info(message == null ? "null" : message, args);
        }
    }

    public static void warn(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isWarnEnabled()) {
            logger.warn(message == null ? "null" : message, args);
        }
    }

    public static void error(String message, Object... args) {
        Logger logger = getLogger();
        if (logger.isErrorEnabled()) {
            logger.error(message == null ? "null" : message, args);
        }
    }
}