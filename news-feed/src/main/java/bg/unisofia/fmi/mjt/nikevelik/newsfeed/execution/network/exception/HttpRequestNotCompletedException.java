package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

public class HttpRequestNotCompletedException extends HttpException {
    public HttpRequestNotCompletedException(String message) {
        super(message);
    }

    public HttpRequestNotCompletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
