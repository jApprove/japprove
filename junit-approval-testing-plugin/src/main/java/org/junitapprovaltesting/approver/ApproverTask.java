package org.junitapprovaltesting.approver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.model.TextFile;

import org.junitapprovaltesting.util.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ApproverTask extends DefaultTask {

    private static Logger LOGGER = LogManager.getLogger(ApproverTask.class);
    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.1.3\\bin\\idea64 diff";

    @Input
    private String fileName;

    @Input
    private boolean approveAll;

    @Option(option = "file", description = "Provides the name of the file that should be approved")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Option(option = "all", description = "All unapproved files should be approved")
    public void setApproveAll() {
        this.approveAll=true;
    }

    @TaskAction
    public void approve() {
        if(approveAll) {
            approveAll();
        } else if (fileName != null) {
            approveFile(FileUtils.getToApprove(fileName));
        } else {
            startBatchProcess();
        }
    }

    private void approveAll() {
        List<File> unapprovedFiles = FileUtils.getToApprove();
        if(unapprovedFiles.size() == 0) {
            LOGGER.info("Found no unapproved files");
            return;
        }
        LOGGER.info("Found " + unapprovedFiles.size() + " unapproved files");
        for (File file : unapprovedFiles) {
            TextFile toApprove = new TextFile(file.getPath());
            this.approveFile(toApprove);
        }
    }

    private void startBatchProcess() {
        List<File> unapprovedFiles = FileUtils.getToApprove();
        if(unapprovedFiles.size() == 0) {
            LOGGER.info("Found no unapproved files");
            return;
        }
        LOGGER.info("Found " + unapprovedFiles.size() + " unapproved files");
        LOGGER.info("Starting batch process ..");
        Scanner scanner = new Scanner(System.in);
        for (File file : unapprovedFiles) {
            TextFile toApprove = new TextFile(file.getPath());
            LOGGER.info("Unapproved file: " + toApprove + ":");
            TextFile baseline;
            try {
                baseline = FileUtils.getBaseline(file.getName().replace("_toApprove.txt", ""));
            } catch (FileNotFoundException e) {
                LOGGER.info("No unapproved version exists");
                LOGGER.info("Approve current version? (y/n)");
                if (userAcceptsRequest(scanner)) {
                    this.approveFile(toApprove);
                }
                continue;
            }
            LOGGER.info("Differences:\n" + formatDifferences(toApprove.computeDifferences(baseline)));
            LOGGER.info("Show entire diff? (y/n)");
            if (userAcceptsRequest(scanner)) {
                String cmd = IDEA_DIFF + " " + toApprove.getPath() + " " + baseline.getPath();
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    throw new RuntimeException("Diff tool " + IDEA_DIFF + " not found!");
                }
            }
            LOGGER.info("Approve current version? (y/n)");
            if (userAcceptsRequest(scanner)) {
                this.approveFile(toApprove);
            }
        }
        scanner.close();
    }

    private boolean userAcceptsRequest(Scanner scanner) {
        String input = scanner.next();
        while (!input.equals("y") && !input.equals("n")) {
            LOGGER.info("Wrong input!");
            input = scanner.next();
        }
        if(input.equals("y")) {
            return true;
        }
        return false;
    }

    private void approveFile(TextFile textFile) {
        TextFile baseline;
        try {
            baseline = FileUtils.getBaseline(textFile.getName().replace("_toApprove.txt", ""));
        } catch (FileNotFoundException e) {
            baseline = FileUtils.createBaseline(textFile.getName().replace("_toApprove.txt", ""));
        }
        try {
            FileUtils.copyFile(textFile, baseline);
            if(textFile.delete()) {
                LOGGER.info("Successfully approved file " + textFile.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException("File "+ textFile.getName() +" not found");
        }
    }

    private String formatDifferences(List<String> differences) {
        StringBuilder builder = new StringBuilder();
        for (String difference : differences) {
            builder.append(difference);
            builder.append("\n");
        }
        return builder.toString();
    }


}
