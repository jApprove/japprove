package org.junitapprovaltesting.approver;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.engine.ApprovalTestingEngine;
import org.junitapprovaltesting.repositories.BaselineRepositoryImpl;

public class ApproverTask extends DefaultTask {

    @Input
    private String baseline;

    @Input
    private boolean approveAll;

    @Option(option = "baseline", description = "Provides the name of the baseline candidate that should be approved")
    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    @Option(option = "all", description = "All unapproved baseline candidates should be approved")
    public void setApproveAll() {
        this.approveAll = true;
    }

    @TaskAction
    public void approve() {
        ApprovalTestingConfiguration approvalTestingConfiguration = new ApprovalTestingConfiguration();
        BaselineRepositoryImpl baselineRepository = new BaselineRepositoryImpl(approvalTestingConfiguration);
        ApprovalTestingEngine approvalTestingEngine =
                new ApprovalTestingEngine(baselineRepository, approvalTestingConfiguration);
        Approver approver = approvalTestingEngine.getApprover();
        if(approveAll) {
            approver.approveAllBaselineCandidates();
        } else if (baseline != null) {
            approver.approveBaselineCandidate(baseline);
        } else {
            approver.startApprovingBatchProcess();
        }
    }

}
