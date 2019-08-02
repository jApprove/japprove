package org.junitapprovaltesting.differ;


import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class DifferTask extends DefaultTask {
    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.1.3\\bin\\idea64 diff";

    private String fileName;

    @Option(option = "file", description = "Provides the name of the file that should be diffed")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @TaskAction
    public void differ() {
        File baseline = FileUtils.getBaseline(fileName);
        if (baseline == null) {
            throw new RuntimeException("Found no baseline for passed file " + fileName);
        }
        File toApprove = FileUtils.getToApprove(fileName);
        if (toApprove == null) {
            throw new RuntimeException("Found no unapproved version for passed file " + fileName);
        }
        String cmd = IDEA_DIFF + " " + toApprove.getPath() + " " + baseline.getPath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Diff tool " + IDEA_DIFF + " not found!");
        }
    }

}