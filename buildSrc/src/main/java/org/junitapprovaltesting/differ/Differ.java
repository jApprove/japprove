package org.junitapprovaltesting.differ;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;

public class Differ extends DefaultTask {
    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2018.3.3\\bin\\idea64 diff";
    private static final String BASELINE_FILE = "baseline";
    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_FILE = "toApprove";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";
    private String filename;

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @TaskAction
    void diff() {
        File baseline = getFile(BASELINE_DIRECTORY, BASELINE_FILE);
        File toApprove = getFile(TO_APPROVE_DIRECTORY, TO_APPROVE_FILE);
        String cmd = IDEA_DIFF + " " + toApprove.getPath() + " " + baseline.getPath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String directory, String prefix) {
        for (File file : new File(directory).listFiles()) {
            if (file.getPath().startsWith(formatFilename(this.filename, directory, prefix))) {
                return file;
            }
        }
        return null;
    }

    private String formatFilename(String filename, String directory, String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(directory).append(prefix).append("_").append(filename);
        return result.toString().replace(".", "_");
    }
}
