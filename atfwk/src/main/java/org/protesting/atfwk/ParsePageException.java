package org.protesting.atfwk;

/**
 * ParsePageException class
 * Author: Alexey Bulat
 * Date: 14.01.2011
 */
public class ParsePageException extends RuntimeException {

    public ParsePageException() {
        super();
    }

    public ParsePageException(String message) {
        super(message);
    }

    public ParsePageException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsePageException(Class pageClass, Throwable cause) {
        this(pageClass.getSimpleName() + " - Page is not parsed correctly", cause);
    }

    public ParsePageException(Throwable cause) {
        super(cause);
    }

}
