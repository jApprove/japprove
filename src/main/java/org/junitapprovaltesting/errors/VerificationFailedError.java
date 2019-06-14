package org.junitapprovaltesting.errors;

import org.opentest4j.AssertionFailedError;

/**
 * This Error is thrown if a verification of an approval test fails.
 */
public class VerificationFailedError extends AssertionFailedError {

    public VerificationFailedError(String message) {
        super(message);
    }
}
