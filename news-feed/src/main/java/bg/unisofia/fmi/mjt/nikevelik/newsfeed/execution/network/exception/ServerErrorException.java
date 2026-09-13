package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception;

public class ServerErrorException extends HttpException {
    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
