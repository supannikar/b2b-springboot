package no.api.exception;

/**
 *
 */
public class B2BException extends RuntimeException {
    public B2BException() {
        super();
    }

    public B2BException(String message) {
        super(message);
    }

    public B2BException(String message, Throwable cause) {
        super(message, cause);
    }

    public B2BException(Throwable cause) {
        super(cause);
    }

}
