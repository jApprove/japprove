package org.japproval.engine;

import org.japproval.approver.Approver;
import org.japproval.config.ApprovalTestingConfiguration;
import org.japproval.differ.Differ;
import org.japproval.repositories.BaselineRepository;
import org.japproval.verifier.JsonVerifier;
import org.japproval.verifier.StringVerifier;

/**
 * The central part of the approval testing application.
 */
public class ApprovalTestingEngine {

    private BaselineRepository baselineRepository;
    private ApprovalTestingConfiguration approvalTestingConfiguration;
    private String baseline;

    public ApprovalTestingEngine(BaselineRepository baselineRepository,
                                 ApprovalTestingConfiguration approvalTestingConfiguration) {
        this(baselineRepository, approvalTestingConfiguration, null);
    }

    public ApprovalTestingEngine(BaselineRepository baselineRepository,
                                 ApprovalTestingConfiguration approvalTestingConfiguration,
                                 String baseline) {
        this.baselineRepository = baselineRepository;
        this.approvalTestingConfiguration = approvalTestingConfiguration;
        this.baseline = baseline;
    }

    /**
     * Returns the injected {@link BaselineRepository}.
     *
     * @return the injected {@link BaselineRepository}.
     */
    public BaselineRepository getBaselineRepository() {
        return this.baselineRepository;
    }

    /**
     * Returns a new {@link Approver}.
     *
     * @return a new {@link Approver}
     */
    public Approver getApprover() {
        return new Approver(this);
    }

    /**
     * Returns a new {@link Differ}.
     *
     * @return a new {@link Differ}
     */
    public Differ getDiffer() {
        return new Differ(this, this.approvalTestingConfiguration.getDiffTool());
    }

    /**
     * Returns a new {@link StringVerifier}.
     *
     * @return a new {@link StringVerifier}
     */
    public StringVerifier getStringVerifier() {
        return new StringVerifier(this);
    }

    /**
     * Returns a new {@link JsonVerifier}.
     *
     * @return a new {@link JsonVerifier}
     */
    public JsonVerifier getJsonVerifier() {
        return new JsonVerifier(this);
    }

    /**
     * Returns the current baseline.
     *
     * @return the current baseline.
     */
    public String getBaseline() {
        return baseline;
    }
}
