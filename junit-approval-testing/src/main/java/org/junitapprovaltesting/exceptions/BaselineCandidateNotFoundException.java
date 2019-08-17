package org.junitapprovaltesting.exceptions;

/**
 * This Exception is thrown if a wanted baseline candidate can not be found.
 */
public class BaselineCandidateNotFoundException extends Exception {

    public BaselineCandidateNotFoundException(String baselineCandidateName) {
        super("Baseline candidate " + baselineCandidateName + " cannot be found");
    }
}
