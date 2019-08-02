package org.junitapprovaltesting.approver;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.model.TextFile;
import org.junitapprovaltesting.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class ApproverTask extends DefaultTask {

    private String fileName;

    @Option(option = "file", description = "Provides the name of the file that should be approved")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @TaskAction
    public void approve() {
        if (fileName == null) {
            this.startBatchProcess();
        } else {
            approveFile(fileName);
        }
    }

    private void startBatchProcess() {
        System.out.println("To be implemented");
        for (File file : FileUtils.getToApprove()) {
            // show differences
            // ask user to show:
            // entire diff
            // approve
            // decline
        }
    }

    private void approveFile(String name) {
        TextFile toApprove = FileUtils.getToApprove(name);
        TextFile baseline = FileUtils.getBaseline(name);
        try {
            FileUtils.copyFile(toApprove, baseline);
            toApprove.delete();
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }
    }

}
