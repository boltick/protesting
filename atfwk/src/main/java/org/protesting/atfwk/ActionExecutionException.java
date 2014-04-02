package org.protesting.atfwk;

/**
 * ActionExecutionException class
 * Author: Alexey Bulat
 * Date: 13/05/11
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
