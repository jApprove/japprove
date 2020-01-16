package org.japprove.exceptions;

/**
 * This Exception is thrown if the content of the baseline candidate cannot be copied to the
 * baseline.
 */
public class CopyingFailedException extends Exception {

    public CopyingFailedException(String message) {
        super(message);
    }
}
