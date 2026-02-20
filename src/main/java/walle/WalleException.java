package walle;

/**
 * Represents an application-specific exception for invalid user commands
 * or storage/parsing errors.
 */
public class WalleException extends Exception {

    /**
     * Creates an exception with the given message.
     *
     * @param message Error message to display to the user.
     */

    public WalleException(String message) {
        super(message);
    }
}
