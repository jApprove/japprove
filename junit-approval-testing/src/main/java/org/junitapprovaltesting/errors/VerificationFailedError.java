package org.junitapprovaltesting.errors;

import org.junitapprovaltesting.model.TextFile;
import org.opentest4j.AssertionFailedError;

/**
 * This Error is thrown if a verification of an approval test fails.
 */
public class VerificationFailedError extends AssertionFailedError {

    public VerificationFailedError(TextFile toApprove, String differences) {
        super("Found differences in " + toApprove.getBaselineName() + ": \n" + differences);
    }
}
