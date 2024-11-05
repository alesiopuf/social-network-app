package ubb.scs.map.domain.exception;

public class DatabaseConnectionException extends RuntimeException {
    private static final String MESSAGE = "Failed to connect to the database";

    public DatabaseConnectionException() {
        super(MESSAGE);
    }
}
