package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

public class NotFoundException extends ClientErrorException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
