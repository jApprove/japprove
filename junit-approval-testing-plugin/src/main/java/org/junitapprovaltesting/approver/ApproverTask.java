package org.junitapprovaltesting.approver;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.tools.Approver;

public class ApproverTask extends DefaultTask {

    @Input
    private String fileName;

    @Input
    private boolean approveAll;

    @Option(option = "file", description = "Provides the name of the file that should be approved")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Option(option = "all", description = "All unapproved files should be approved")
    public void setApproveAll() {
        this.approveAll=true;
    }

    @TaskAction
    public void approve() {
        ApprovalTestingConfiguration config = new ApprovalTestingConfiguration();
        Approver approver = new Approver(config);
        if(approveAll) {
            approver.approveAllFiles();
        } else if (fileName != null) {
            approver.approveFile(fileName);
        } else {
            approver.startApprovingBatchProcess();
        }
    }

}
