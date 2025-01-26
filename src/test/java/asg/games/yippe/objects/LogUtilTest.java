package asg.games.yippe.objects;

import asg.games.yipee.tools.LogUtil;
import org.junit.jupiter.api.Test;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogUtilTest {

    @Test
    void testTraceLog() {
        TestLogger logger = TestLoggerFactory.getTestLogger(LogUtilTest.class);
        LogUtil.trace("Trace message");
        assertEquals(1, logger.getLoggingEvents().size());
        assertEquals("Trace message", logger.getLoggingEvents().get(0).getMessage());
    }

    @Test
    void testDebugLogWithArgs() {
        TestLogger logger = TestLoggerFactory.getTestLogger(LogUtilTest.class);
        LogUtil.debug("Debug message with arg: {}", "argument");
        assertEquals(1, logger.getLoggingEvents().size());
        assertEquals("Debug message with arg: argument", logger.getLoggingEvents().get(0).getMessage());
    }

    @Test
    void testInfoLog() {
        TestLogger logger = TestLoggerFactory.getTestLogger(LogUtilTest.class);
        LogUtil.info("Info message");
        assertEquals(1, logger.getLoggingEvents().size());
        assertEquals("Info message", logger.getLoggingEvents().get(0).getMessage());
    }

    @Test
    void testWarnLog() {
        TestLogger logger = TestLoggerFactory.getTestLogger(LogUtilTest.class);
        LogUtil.warn("Warning message");
        assertEquals(1, logger.getLoggingEvents().size());
        assertEquals("Warning message", logger.getLoggingEvents().get(0).getMessage());
    }

    @Test
    void testErrorLogWithNullArgs() {
        TestLogger logger = TestLoggerFactory.getTestLogger(LogUtilTest.class);
        LogUtil.error("Error message", (Object[]) null);
        assertEquals(1, logger.getLoggingEvents().size());
        assertEquals("Error message", logger.getLoggingEvents().get(0).getMessage());
    }
}