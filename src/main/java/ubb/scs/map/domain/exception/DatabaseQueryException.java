package ubb.scs.map.domain.exception;

public class DatabaseQueryException extends RuntimeException {
    private static final String MESSAGE = "Failed to execute query: %s";

    public DatabaseQueryException(String query) {
        super(String.format(MESSAGE, query));
    }
}
