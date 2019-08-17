package org.junitapprovaltesting.exceptions;

/**
 * This Exception is thrown if a baseline can not be created.
 */
public class BaselineCandidateCreationFailedException extends Exception {

    public BaselineCandidateCreationFailedException(String baselineCandidate) {
        super(baselineCandidate + " cannot be created");
    }
}
