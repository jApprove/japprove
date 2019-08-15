package org.junitapprovaltesting.exceptions;

/**
 * This Exception is thrown if a wanted approved file can not be found.
 */
public class ApprovedFileNotFoundException extends RuntimeException {

    public ApprovedFileNotFoundException(String filename) {
        super("Unapproved file " + filename + " cannot be found");
    }
}
