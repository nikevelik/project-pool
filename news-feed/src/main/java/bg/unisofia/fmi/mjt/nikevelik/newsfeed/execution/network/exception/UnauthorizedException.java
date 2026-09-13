package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

public class UnauthorizedException extends ClientErrorException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
