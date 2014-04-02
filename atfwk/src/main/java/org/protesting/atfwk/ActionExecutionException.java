package org.protesting.atfwk;

/**
 * ActionExecutionException class
 * User: AB83625
 * Date: 13/05/11
 * To change this template use File | Settings | File Templates.
 */
public class ActionExecutionException extends RuntimeException {

    public ActionExecutionException() {
        super();
    }

    public ActionExecutionException(String message) {
        super(message);
    }

    public ActionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionExecutionException(Throwable cause) {
        super(cause);
    }
}
