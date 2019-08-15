package org.junitapprovaltesting.approver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.differ.Differ;
import org.junitapprovaltesting.exceptions.ApprovingFailedException;
import org.junitapprovaltesting.exceptions.UnapprovedFileNotFoundException;
import org.junitapprovaltesting.files.ApprovableFile;
import org.junitapprovaltesting.services.FileService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Approver {

    private static Logger LOGGER = LogManager.getLogger(Approver.class);
    private FileService fileService;
    private ApprovalTestingConfiguration config;

    public Approver(ApprovalTestingConfiguration config) {
        this.config = config;
        fileService = new FileService(config);
    }

    /**
     * Approves a file by its name.
     *
     * @param filename the name of the file
     */
    public void approveFile(String filename) {
        ApprovableFile unapprovedFile;
        try {
            unapprovedFile = fileService.getUnapprovedFile(filename);
        } catch (FileNotFoundException e) {
            throw new UnapprovedFileNotFoundException(filename);
        }
        approveFile(unapprovedFile);
    }

    /**
     * Approves a passed {@link ApprovableFile}.
     *
     * @param unapprovedFile the {@link ApprovableFile} that should be approved
     */
    public void approveFile(ApprovableFile unapprovedFile) {
        ApprovableFile baseline;
        try {
            baseline = fileService.getBaseline(unapprovedFile.getName());
        } catch (FileNotFoundException e) {
            baseline = fileService.createBaseline(unapprovedFile.getName());
        }
        try {
            copyFile(unapprovedFile, baseline);
            if (unapprovedFile.delete()) {
                LOGGER.info("Successfully approved file " + unapprovedFile.getName());
            }
        } catch (IOException e) {
            throw new ApprovingFailedException(unapprovedFile.getName());
        }
    }

    /**
     * Approves all files in the toApprove directory.
     */
    public void approveAllFiles() {
        List<ApprovableFile> unapprovedFiles = fileService.getUnapprovedFiles();
        if (unapprovedFiles.size() == 0) {
            LOGGER.info("Found no unapproved files");
            return;
        }
        LOGGER.info("Found " + unapprovedFiles.size() + " unapproved files");
        for (ApprovableFile file : unapprovedFiles) {
            approveFile(file);
        }
    }

    /**
     * Starts a batch process to approve or diff all files step by step.
     */
    public void startApprovingBatchProcess() {
        List<ApprovableFile> unapprovedFiles = fileService.getUnapprovedFiles();
        if (unapprovedFiles.size() == 0) {
            LOGGER.info("Found no unapproved files");
            return;
        }
        LOGGER.info("Found " + unapprovedFiles.size() + " unapproved files");
        LOGGER.info("Starting batch process ..");
        Scanner scanner = new Scanner(System.in);
        Differ differ = new Differ(config);
        for (ApprovableFile unapprovedFile : unapprovedFiles) {
            LOGGER.info("Unapproved file: " + unapprovedFile.getName());
            ApprovableFile baseline;
            try {
                baseline = fileService.getBaseline(unapprovedFile.getName());
            } catch (FileNotFoundException e) {
                LOGGER.info("No approved version exists");
                LOGGER.info("Approve current version? (y/n)");
                if (userAcceptsRequest(scanner)) {
                    approveFile(unapprovedFile);
                }
                continue;
            }
            LOGGER.info("Differences:\n" + formatDifferences(unapprovedFile.computeDifferences(baseline)));
            LOGGER.info("Show entire diff? (y/n)");
            if (userAcceptsRequest(scanner)) {
                differ.diff(unapprovedFile.getName());
            }
            LOGGER.info("Approve current version? (y/n)");
            if (userAcceptsRequest(scanner)) {
                approveFile(unapprovedFile);
            }
        }
        scanner.close();
    }

    private void copyFile(ApprovableFile toApprove, ApprovableFile baseline) throws IOException {
        FileInputStream inputStream = new FileInputStream(toApprove);
        FileOutputStream outputStream = new FileOutputStream(baseline);
        inputStream.getChannel().transferTo(0, toApprove.length(), outputStream.getChannel());
        inputStream.close();
        outputStream.close();
    }

    private boolean userAcceptsRequest(Scanner scanner) {
        String input = scanner.next();
        while (!input.equals("y") && !input.equals("n")) {
            LOGGER.info("Wrong input!");
            input = scanner.next();
        }
        if (input.equals("y")) {
            return true;
        }
        return false;
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
