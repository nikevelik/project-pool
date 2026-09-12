package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

public class BadJsonInputException extends RuntimeException {

    public BadJsonInputException(String message) {
        super(message);
    }

    public BadJsonInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
