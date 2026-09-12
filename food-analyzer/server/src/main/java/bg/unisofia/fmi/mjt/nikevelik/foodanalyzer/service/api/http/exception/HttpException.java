package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.exception;

public class HttpException extends RuntimeException {

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable e) {
        super(message, e);
    }
}
