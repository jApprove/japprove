package org.japprove.exceptions;

/**
 * This Exception is thrown if a baseline can not be created.
 */
public class BaselineCreationFailedException extends Exception {

    public BaselineCreationFailedException(String baseline) {
        super(baseline + " cannot be created");
    }
}
