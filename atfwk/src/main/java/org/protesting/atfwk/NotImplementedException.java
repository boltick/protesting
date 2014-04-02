package org.protesting.atfwk;

/**
 * NotImplementedException class
 * User: ab83625
 * Date: 26.01.2010
 * To change this template use File | Settings | File Templates.
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
