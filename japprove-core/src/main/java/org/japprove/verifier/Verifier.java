package org.japprove.verifier;

import org.japprove.differ.Differ;
import org.japprove.engine.ApprovalTestingEngine;
import org.japprove.repositories.BaselineRepositoryImpl;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    String baselineName;
    BaselineRepositoryImpl baselineRepository;
    Differ differ;

    public Verifier(ApprovalTestingEngine approvalTestingEngine) {
        this.baselineRepository =
                (BaselineRepositoryImpl) approvalTestingEngine.getBaselineRepository();
        this.baselineName = approvalTestingEngine.getBaseline();
        this.differ = approvalTestingEngine.getDiffer();
    }
}
