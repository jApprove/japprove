package org.japprove.approver;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.japprove.config.ApprovalTestingConfiguration;
import org.japprove.engine.ApprovalTestingEngine;
import org.japprove.repositories.BaselineRepositoryImpl;

@Mojo(name = "approve")
public class ApproverMojo extends AbstractMojo {

    @Parameter(property = "baseline")
    private String baseline;

    @Parameter(property = "all")
    private boolean approveAll;

    /**
     * The task that contains the options to approve one or more baseline candidates.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        ApprovalTestingConfiguration approvalTestingConfiguration =
                new ApprovalTestingConfiguration();
        BaselineRepositoryImpl baselineRepository =
                new BaselineRepositoryImpl(approvalTestingConfiguration);
        ApprovalTestingEngine approvalTestingEngine =
                new ApprovalTestingEngine(baselineRepository, approvalTestingConfiguration);
        Approver approver = approvalTestingEngine.getApprover();
        if (approveAll) {
            approver.approveAllBaselineCandidates();
        } else if (baseline != null) {
            approver.approveBaselineCandidate(baseline);
        } else {
            approver.startApprovingBatchProcess();
        }
    }
}
