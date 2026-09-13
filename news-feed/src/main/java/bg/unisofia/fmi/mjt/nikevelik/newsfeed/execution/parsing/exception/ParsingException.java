package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.SearchException;

public class ParsingException extends SearchException {
    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
