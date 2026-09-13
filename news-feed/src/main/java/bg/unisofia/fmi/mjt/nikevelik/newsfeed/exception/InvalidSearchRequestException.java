package bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception;

public class InvalidSearchRequestException extends InvalidClientInputException {
    public InvalidSearchRequestException(String message) {
        super(message);
    }

    public InvalidSearchRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
