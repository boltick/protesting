package org.protesting.atfwk;

/**
 * ContextInitializationException class
 * Author: Alexey Bulat
 * Date: 16.04.2010
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

