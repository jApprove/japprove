package org.junitapprovaltesting.util;

import org.junitapprovaltesting.model.TextFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String BASELINE_DIRECTORY = "baselines" + File.separator;
    private static final String TO_APPROVE_DIRECTORY = "build" + File.separator + "approvals" + File.separator;
    static final String TO_APPROVE_FILE = "_toApprove";
    static final String TXT_ENDING = ".txt";

    public static List<File> getToApprove() {
        File directory = new File(TO_APPROVE_DIRECTORY);
        List<File> files = new ArrayList<>();
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                files.add(file);
            }
        }
        return files;
    }

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

    public static TextFile getBaseline(String fileName) throws FileNotFoundException {
        File directory = new File(BASELINE_DIRECTORY);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(BASELINE_DIRECTORY + fileName + TXT_ENDING)) {
                    return new TextFile(file.getPath());
                }
            }
        }
        throw new FileNotFoundException("Found no approved version for passed file " + fileName);
    }

    public static TextFile createBaseline(String fileName) {
        TextFile baseline = new TextFile(BASELINE_DIRECTORY + fileName + TXT_ENDING);
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
