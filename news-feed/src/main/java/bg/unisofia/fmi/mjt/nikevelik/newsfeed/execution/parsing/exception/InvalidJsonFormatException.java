package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception;

public class InvalidJsonFormatException extends ParsingException {
    public InvalidJsonFormatException(String message) {
        super(message);
    }

    public InvalidJsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
