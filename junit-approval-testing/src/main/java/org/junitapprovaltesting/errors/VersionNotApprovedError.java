package org.junitapprovaltesting.errors;

import org.junitapprovaltesting.model.TextFile;
import org.opentest4j.AssertionFailedError;

public class VersionNotApprovedError extends AssertionFailedError {
    public VersionNotApprovedError(TextFile path) {
        super("Not approved: " + path);
    }
}
