package org.junitapprovaltesting.approver;


import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Approver extends DefaultTask {
    private static final String BASELINE_FILE = "baseline";
    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_FILE = "toApprove";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";
    private String filename;

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @TaskAction
    void approve() {
        File baseline = getFile(BASELINE_DIRECTORY, BASELINE_FILE);
        File toApprove = getFile(TO_APPROVE_DIRECTORY, TO_APPROVE_FILE);
        try {
            copy(toApprove, baseline);
        } catch (IOException e) {
            e.printStackTrace();
        }
        toApprove.delete();
    }

    private void copy(File toApprove, File baseline) throws IOException {
        FileInputStream inputStream = new FileInputStream(toApprove);
        FileOutputStream outputStream = new FileOutputStream(baseline);
        inputStream.getChannel().transferTo(0, toApprove.length(), outputStream.getChannel());
        inputStream.close();
        outputStream.close();
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
