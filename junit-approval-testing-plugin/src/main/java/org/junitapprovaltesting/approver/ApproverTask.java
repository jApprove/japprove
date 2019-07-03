package org.junitapprovaltesting.approver;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.junitapprovaltesting.PluginExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ApproverTask extends DefaultTask {

    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";

    @TaskAction
    public void samplePluginTasks() throws TaskExecutionException {
        try {
            PluginExtension extension = getProject().getExtensions().findByType(PluginExtension.class);
            String filePath = extension.getFileName();

            File baseline = getFile(BASELINE_DIRECTORY, filePath);
            File toApprove = getFile(TO_APPROVE_DIRECTORY, filePath);

            copy(toApprove, baseline);
            toApprove.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copy(File toApprove, File baseline) throws IOException {
        FileInputStream inputStream = new FileInputStream(toApprove);
        FileOutputStream outputStream = new FileOutputStream(baseline);
        inputStream.getChannel().transferTo(0, toApprove.length(), outputStream.getChannel());
        inputStream.close();
        outputStream.close();
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
