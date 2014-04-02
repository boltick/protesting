package org.protesting.atfwk;

/**
 * ContextInitializationException class
 * User: ab83625
 * Date: 16.04.2010
 * To change this template use File | Settings | File Templates.
 */
public class ContextInitializationException extends RuntimeException {

    public ContextInitializationException() {
        super();
    }

    public ContextInitializationException(String message) {
        super(message);
    }

    public ContextInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextInitializationException(Throwable cause) {
        super(cause);
    }
}

