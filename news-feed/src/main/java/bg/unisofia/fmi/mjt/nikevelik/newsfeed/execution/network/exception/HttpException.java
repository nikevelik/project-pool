package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.SearchException;

public class HttpException extends SearchException {
    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
