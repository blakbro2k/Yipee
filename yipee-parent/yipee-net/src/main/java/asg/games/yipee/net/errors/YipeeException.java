package asg.games.yipee.net.errors;

/**
 * Base class for all game-related runtime exceptions.
 * These should be thrown from request handlers and intercepted
 * at the networking boundary to produce appropriate ErrorResponses.
 */
public class YipeeException extends RuntimeException {

    public YipeeException(String message) {
        super(message);
    }

    public YipeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
