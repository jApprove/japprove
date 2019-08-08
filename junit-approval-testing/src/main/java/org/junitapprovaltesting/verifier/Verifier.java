package org.junitapprovaltesting.verifier;

import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.services.FileService;

import java.util.List;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    final String baselineName;
    FileService fileService;

    public Verifier(String baselineName) {
        this.baselineName = baselineName;
        ApprovalTestingConfiguration config = new ApprovalTestingConfiguration();
        fileService = new FileService(config);
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
