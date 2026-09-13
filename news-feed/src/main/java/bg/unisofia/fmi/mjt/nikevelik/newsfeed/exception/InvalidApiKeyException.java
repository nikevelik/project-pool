package bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception;

public class InvalidApiKeyException extends InvalidClientInputException {
    public InvalidApiKeyException(String message) {
        super(message);
    }

    public InvalidApiKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
