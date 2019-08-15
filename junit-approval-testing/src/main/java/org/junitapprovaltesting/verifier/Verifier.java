package org.junitapprovaltesting.verifier;

import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.services.FileService;

import java.util.List;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    String baseline;
    FileService fileService;

    public Verifier(String baseline) {
        this.baseline = baseline;
        fileService = new FileService(new ApprovalTestingConfiguration());
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
