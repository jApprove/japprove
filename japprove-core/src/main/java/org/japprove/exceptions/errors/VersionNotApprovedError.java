package org.japprove.exceptions.errors;

import org.opentest4j.AssertionFailedError;

/**
 * This Error is thrown if no approved version within an approval test exists.
 */
public class VersionNotApprovedError extends AssertionFailedError {

    public VersionNotApprovedError(String baselineName) {
        super("Not approved: " + baselineName);
    }
}
