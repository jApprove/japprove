package org.junitapprovaltesting.exceptions;

/**
 * This Exception is thrown if a wanted baseline can not be found.
 */
public class BaselineNotFoundException extends RuntimeException {

    public BaselineNotFoundException(String filename) {
        super("Baseline " + filename + " cannot be found");
    }
}
