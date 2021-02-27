package org.sid.exceptions;

@SuppressWarnings("serial")
public class SpringRedditException extends RuntimeException {

    public SpringRedditException(String message) {
	super(message);
    }
}
