package org.junitapprovaltesting.verifier;

import org.junitapprovaltesting.differ.Differ;
import org.junitapprovaltesting.engine.ApprovalTestingEngine;
import org.junitapprovaltesting.repositories.BaselineRepositoryImpl;

/**
 * The parent class of the verifiers.
 */
public abstract class Verifier {

    String baseline;
    BaselineRepositoryImpl baselineRepository;
    Differ differ;

    public Verifier(ApprovalTestingEngine approvalTestingEngine) {
        this.baselineRepository = (BaselineRepositoryImpl) approvalTestingEngine.getBaselineRepository();
        this.baseline = approvalTestingEngine.getBaseline();
        this.differ = approvalTestingEngine.getDiffer();
    }

}
