package org.junitapprovaltesting.exceptions;

/**
 * This Exception is thrown if a wanted unapproved file can not be found.
 */
public class UnapprovedFileNotFoundException extends RuntimeException {

    public UnapprovedFileNotFoundException(String filename) {
        super("Unapproved file " + filename + " cannot be found");
    }
}
