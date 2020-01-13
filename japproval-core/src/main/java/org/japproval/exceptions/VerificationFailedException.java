package org.japproval.exceptions;

/**
 * This Exception is thrown if the differences of two Files cannot be computed.
 */
public class VerificationFailedException extends RuntimeException {

    public VerificationFailedException(String message) {super(message);}
}
