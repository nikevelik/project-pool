package bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception;

public class InvalidClientInputException extends SearchException {
    public InvalidClientInputException(String message) {
        super(message);
    }

    public InvalidClientInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
