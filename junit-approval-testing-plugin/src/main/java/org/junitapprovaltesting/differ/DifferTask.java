package org.junitapprovaltesting.differ;


import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.junitapprovaltesting.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class DifferTask extends DefaultTask {
    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.1.3\\bin\\idea64 diff";
    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";

    @TaskAction
    public void differ() {
        DifferPluginExtension extension = getProject().getExtensions().findByType(DifferPluginExtension.class);
        String fileName = extension.getFileName();

        File baseline = FileUtils.getFile(BASELINE_DIRECTORY, fileName);
        if (baseline == null) {
            throw new RuntimeException("Found no baseline for passed file " + fileName);
        }
        File toApprove = FileUtils.getFile(TO_APPROVE_DIRECTORY, fileName);
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