package org.junitapprovaltesting.exceptions;

/**
 * This Exception is thrown if a wanted baseline candidate can not be found.
 */
public class BaselineCandidateNotFoundException extends RuntimeException {

    public BaselineCandidateNotFoundException(String filename) {
        super("Baseline candidate " + filename + " cannot be found");
    }
}