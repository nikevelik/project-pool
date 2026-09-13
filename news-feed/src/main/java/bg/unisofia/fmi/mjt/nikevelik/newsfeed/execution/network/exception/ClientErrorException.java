package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

public class ClientErrorException extends HttpException {
    public ClientErrorException(String message) {
        super(message);
    }

    public ClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
