package org.japprove.exceptions;

/**
 * This Exception is thrown if the differences of two Files cannot be computed.
 */
public class DiffingFailedException extends RuntimeException {

    public DiffingFailedException(String message) {
        super(message);
    }
}
