package org.junitapprovaltesting.approver;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.junitapprovaltesting.model.TextFile;
import org.junitapprovaltesting.util.FileUtils;

import java.io.IOException;

public class ApproverTask extends DefaultTask {

    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";
    private static final String TO_APPROVE_SUFFIX = "_toApprove";

    @TaskAction
    public void approve() {
        ApproverPluginExtension extension = getProject().getExtensions().findByType(ApproverPluginExtension.class);
        String fileName = extension.getFileName();

        TextFile toApprove = FileUtils.getFile(TO_APPROVE_DIRECTORY, fileName);
        if (toApprove == null) {
            throw new RuntimeException("Found no unapproved version for passed file " + fileName);
        }
        TextFile baseline = FileUtils.getFile(BASELINE_DIRECTORY, fileName);
        if (baseline == null) {
            baseline = createBaseline(toApprove);
        }
        try {
            FileUtils.copyFile(toApprove, baseline);
            toApprove.delete();
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }
    }

    private TextFile createBaseline(TextFile toApprove) {
        TextFile baseline;
        String baselineName = toApprove.getName().replace(TO_APPROVE_SUFFIX, "");
        baseline = new TextFile(BASELINE_DIRECTORY + baselineName);
        try {
            baseline.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create baseline");
        }
        return baseline;
    }
}
