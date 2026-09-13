package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

public class ForbiddenException extends ClientErrorException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
