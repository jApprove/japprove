package org.japproval.verifier;

import org.japproval.differ.Differ;
import org.japproval.engine.ApprovalTestingEngine;
import org.japproval.repositories.BaselineRepositoryImpl;

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
