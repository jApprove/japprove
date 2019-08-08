package org.junitapprovaltesting.exceptions;

/**
 * This Exception is thrown if the differences of two Files cannot be computed.
 */
public class DiffingFailedException extends RuntimeException {

    public DiffingFailedException(String filename) {super("Error while approving " + filename);}
}
