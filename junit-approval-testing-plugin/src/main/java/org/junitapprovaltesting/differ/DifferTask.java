package org.junitapprovaltesting.differ;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.junitapprovaltesting.PluginExtension;

import java.io.File;
import java.io.IOException;

public class DifferTask extends DefaultTask {
    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2018.3.3\\bin\\idea64 diff";
    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";

    @TaskAction
    public void samplePluginTasks() {
        try {
            PluginExtension extension = getProject().getExtensions().findByType(PluginExtension.class);
            String filePath = extension.getFileName();

            File baseline = getFile(BASELINE_DIRECTORY, filePath);
            File toApprove = getFile(TO_APPROVE_DIRECTORY, filePath);

            String cmd = IDEA_DIFF + " " + toApprove.getPath() + " " + baseline.getPath();
            Runtime.getRuntime().exec(cmd);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getFile(String directory, String filename) {
        for (File file : new File(directory).listFiles()) {
            if (file.getPath().startsWith(formatFilename(filename, directory))) {
                return file;
            }
        }
        return null;
    }

    private String formatFilename(String filename, String directory) {
        StringBuilder result = new StringBuilder();
        result.append(directory).append(filename);
        return result.toString();
    }
}