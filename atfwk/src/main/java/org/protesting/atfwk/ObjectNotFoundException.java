package org.protesting.atfwk;

/**
 * ObjectNotFoundException class
 * User: ab83625
 * Date: 10.06.2009
 * To change this template use File | Settings | File Templates.
 */
public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException() {
        super();
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
