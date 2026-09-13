package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception;

public class EmptyInputParsingException extends ParsingException {
    public EmptyInputParsingException(String message) {
        super(message);
    }

    public EmptyInputParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
