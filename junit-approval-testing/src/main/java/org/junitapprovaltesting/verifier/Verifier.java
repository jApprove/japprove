package org.junitapprovaltesting.verifier;

import java.io.File;
import java.util.List;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    static final String TO_APPROVE_FILE = "_toApprove";
    static final String TXT_ENDING = ".txt";
    static final String BASELINE_DIRECTORY = "baselines" + File.separator;
    static final String TO_APPROVE_DIRECTORY = "build" + File.separator + "approvals" + File.separator;
    final String testName;

    public Verifier(String testName) {
        this.testName = testName;
    }

    String formatDifferences(List<String> differences) {
        StringBuilder builder = new StringBuilder();
        for (String difference : differences) {
            builder.append(difference);
            builder.append("\n");
        }
        return builder.toString();
    }
}
