package org.protesting.atfwk;

/**
 * NotImplementedException class
 * Author: Alexey Bulat
 * Date: 26.01.2010
 */
public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
        super("Method is not implemented yet");
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }
}
