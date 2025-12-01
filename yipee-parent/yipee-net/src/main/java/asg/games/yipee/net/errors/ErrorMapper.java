package asg.games.yipee.net.errors;

public class ErrorMapper {
    private ErrorMapper() {
    }

    public static ErrorCode toCode(Throwable t) {
        if (t instanceof YipeeAuthException) {
            return ErrorCode.UNAUTHORIZED;
        }
        if (t instanceof YipeeSessionException) {
            return ErrorCode.INVALID_SESSION;
        }
        if (t instanceof YipeeSyncException) {
            return ErrorCode.OUT_OF_SYNC;
        }
        if (t instanceof YipeeActionException) {
            return ErrorCode.ILLEGAL_ACTION;
        }
        if (t instanceof YipeeBadRequestException) {
            return ErrorCode.BAD_REQUEST;
        }

        return ErrorCode.INTERNAL_ERROR;
    }
}
