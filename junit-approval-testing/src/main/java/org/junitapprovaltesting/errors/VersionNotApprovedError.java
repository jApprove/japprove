package org.junitapprovaltesting.errors;

import org.junitapprovaltesting.model.TextFile;
import org.opentest4j.AssertionFailedError;

/**
 * This Error is thrown if no approved version within an approval test exists.
 */
public class VersionNotApprovedError extends AssertionFailedError {
    public VersionNotApprovedError(TextFile textFile) {
        super("Not approved: " + textFile.getBaselineName());
    }
}
