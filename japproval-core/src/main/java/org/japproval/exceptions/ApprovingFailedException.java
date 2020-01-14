package org.japproval.exceptions;

/**
 * This Exception is thrown if a unapproved File cannot be approved.
 */
public class ApprovingFailedException extends RuntimeException {

    public ApprovingFailedException(String message) {
        super(message);
    }
}

