package org.junitapprovaltesting.verifier;

import java.util.List;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    final String baselineName;

    public Verifier(String baselineName) {
        this.baselineName = baselineName;
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
