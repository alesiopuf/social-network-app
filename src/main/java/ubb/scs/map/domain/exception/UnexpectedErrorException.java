package ubb.scs.map.domain.exception;

public class UnexpectedErrorException extends RuntimeException {
    public UnexpectedErrorException() {
    }

    public UnexpectedErrorException(String message) {
        super(message);
    }

    public UnexpectedErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedErrorException(Throwable cause) {
        super(cause);
    }

    public UnexpectedErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
