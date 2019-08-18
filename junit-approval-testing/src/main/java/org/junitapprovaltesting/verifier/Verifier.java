package org.junitapprovaltesting.verifier;

import org.junitapprovaltesting.engine.ApprovalTestingEngine;
import org.junitapprovaltesting.repositories.BaselineRepositoryImpl;

import java.util.List;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    String baseline;
    BaselineRepositoryImpl baselineRepository;

    public Verifier(ApprovalTestingEngine approvalTestingEngine) {
        this.baselineRepository = (BaselineRepositoryImpl) approvalTestingEngine.getBaselineRepository();
        this.baseline = approvalTestingEngine.getBaseline();
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
