package org.junitapprovaltesting.util;

import org.junitapprovaltesting.model.TextFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";
    static final String TO_APPROVE_FILE = "_toApprove";
    static final String TXT_ENDING = ".txt";

    public static TextFile getToApprove(String fileName) {
        File directory = new File(TO_APPROVE_DIRECTORY);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(TO_APPROVE_DIRECTORY + fileName + TO_APPROVE_FILE + TXT_ENDING)) {
                    return new TextFile(file.getPath());
                }
            }
        }
        throw new RuntimeException("Found no unapproved version for passed file " + fileName);
    }

    public static TextFile getBaseline(String filename) {
        File directory = new File(BASELINE_DIRECTORY);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(BASELINE_DIRECTORY + filename + TXT_ENDING)) {
                    return new TextFile(file.getPath());
                }
            }
        }
        return createBaseline(filename);
    }

    private static TextFile createBaseline(String fileName) {
        TextFile baseline;
        String baselineName = fileName + TXT_ENDING;
        baseline = new TextFile(BASELINE_DIRECTORY + baselineName);
        try {
            baseline.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create baseline");
        }
        return baseline;
    }

    public static void copyFile(TextFile toApprove, TextFile baseline) throws IOException {
        FileInputStream inputStream = new FileInputStream(toApprove);
        FileOutputStream outputStream = new FileOutputStream(baseline);
        inputStream.getChannel().transferTo(0, toApprove.length(), outputStream.getChannel());
        inputStream.close();
        outputStream.close();
    }
}
