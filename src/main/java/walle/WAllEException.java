package walle;

/**
 * Represents an application-specific exception for invalid user commands
 * or storage/parsing errors.
 */
public class WAllEException extends Exception {

    /**
     * Creates an exception with the given message.
     *
     * @param message Error message to display to the user.
     */

    public WAllEException(String message) {
        super(message);
    }
}