package org.junitapprovaltesting.verifier;

import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.services.BaselineRepository;

import java.util.List;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    String baseline;
    BaselineRepository baselineRepository;

    public Verifier(String baseline) {
        this.baseline = baseline;
        baselineRepository = new BaselineRepository(new ApprovalTestingConfiguration());
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
