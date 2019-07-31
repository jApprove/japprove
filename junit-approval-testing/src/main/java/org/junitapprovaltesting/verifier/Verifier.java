package org.junitapprovaltesting.verifier;

import java.util.List;

public abstract class Verifier {

    static final String BASELINE_DIRECTORY = "baselines\\";
    static final String TO_APPROVE_FILE = "_toApprove";
    static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";
    static final String TXT_ENDING = ".txt";
    String baselineFileName;
    String toApproveFileName;

    public Verifier(String testName) {
        this.baselineFileName = BASELINE_DIRECTORY + testName + TXT_ENDING;
        this.toApproveFileName = TO_APPROVE_DIRECTORY + testName + TO_APPROVE_FILE + TXT_ENDING;
    }

    String getErrorMessage(List<String> differences) {
        StringBuilder builder = new StringBuilder();
        for (String difference : differences) {
            builder.append(difference);
            builder.append("\n");
        }
        return builder.toString();
    }
}
