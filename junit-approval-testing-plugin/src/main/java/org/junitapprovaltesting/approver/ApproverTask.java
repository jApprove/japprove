package org.junitapprovaltesting.approver;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.junitapprovaltesting.model.TextFile;
import org.junitapprovaltesting.util.FileUtils;

import java.io.IOException;

public class ApproverTask extends DefaultTask {

    @TaskAction
    public void approve() {
        ApproverPluginExtension extension = getProject().getExtensions().findByType(ApproverPluginExtension.class);
        String fileName = extension.getFileName();

        TextFile toApprove = FileUtils.getToApprove(fileName);
        TextFile baseline = FileUtils.getBaseline(fileName);

        try {
            FileUtils.copyFile(toApprove, baseline);
            toApprove.delete();
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }
    }

}
