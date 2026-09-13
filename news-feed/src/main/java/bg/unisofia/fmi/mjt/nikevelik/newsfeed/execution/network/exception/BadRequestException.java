package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

public class BadRequestException extends ClientErrorException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
