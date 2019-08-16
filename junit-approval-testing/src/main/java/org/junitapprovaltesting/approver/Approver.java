package org.junitapprovaltesting.approver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.differ.Differ;
import org.junitapprovaltesting.exceptions.ApprovingFailedException;
import org.junitapprovaltesting.exceptions.BaselineCandidateNotFoundException;
import org.junitapprovaltesting.files.ApprovableFile;
import org.junitapprovaltesting.services.BaselineRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Approver {

    private static Logger LOGGER = LogManager.getLogger(Approver.class);
    private BaselineRepository baselineRepository;
    private ApprovalTestingConfiguration config;

    public Approver() {
        config = new ApprovalTestingConfiguration();
        baselineRepository = new BaselineRepository(config);
    }

    /**
     * Approves a file by its name.
     *
     * @param filename the name of the file
     */
    public void approveFile(String filename) {
        ApprovableFile baselineCandidate;
        try {
            baselineCandidate = baselineRepository.getBaselineCandidate(filename);
        } catch (FileNotFoundException e) {
            throw new BaselineCandidateNotFoundException(filename);
        }
        approveFile(baselineCandidate);
    }

    /**
     * Approves a passed {@link ApprovableFile}.
     *
     * @param baselineCandidate the {@link ApprovableFile} that should be approved
     */
    public void approveFile(ApprovableFile baselineCandidate) {
        ApprovableFile baseline;
        try {
            baseline = baselineRepository.getBaseline(baselineCandidate.getName());
        } catch (FileNotFoundException e) {
            baseline = baselineRepository.createBaseline(baselineCandidate.getName());
        }
        try {
            copyToBaseline(baselineCandidate, baseline);
            if (baselineCandidate.delete()) {
                LOGGER.info("Successfully approved file " + baselineCandidate.getName());
            }
        } catch (IOException e) {
            throw new ApprovingFailedException(baselineCandidate.getName());
        }
    }

    /**
     * Approves all files in the baselineCandidate directory.
     */
    public void approveAllFiles() {
        List<ApprovableFile> baselineCandidates = baselineRepository.getBaselineCandidates();
        if (baselineCandidates.size() == 0) {
            LOGGER.info("Found no baseline candidates");
            return;
        }
        LOGGER.info("Found " + baselineCandidates.size() + " baseline candidates");
        for (ApprovableFile file : baselineCandidates) {
            approveFile(file);
        }
    }

    /**
     * Starts a batch process to approve or diff all files step by step.
     */
    public void startApprovingBatchProcess() {
        List<ApprovableFile> baselineCandidates = baselineRepository.getBaselineCandidates();
        if (baselineCandidates.size() == 0) {
            LOGGER.info("Found no baseline candidates");
            return;
        }
        LOGGER.info("Found " + baselineCandidates.size() + " baseline candidates");
        LOGGER.info("Starting batch process ..");
        Scanner scanner = new Scanner(System.in);
        Differ differ = new Differ();
        for (ApprovableFile baselineCandidate : baselineCandidates) {
            LOGGER.info("Baseline candidate: " + baselineCandidate.getName());
            ApprovableFile baseline;
            try {
                baseline = baselineRepository.getBaseline(baselineCandidate.getName());
            } catch (FileNotFoundException e) {
                LOGGER.info("No baseline exists");
                LOGGER.info("Approve current version? (y/n)");
                if (userAcceptsRequest(scanner)) {
                    approveFile(baselineCandidate);
                }
                continue;
            }
            LOGGER.info("Differences:\n" + formatDifferences(baselineCandidate.computeDifferences(baseline)));
            LOGGER.info("Show entire diff? (y/n)");
            if (userAcceptsRequest(scanner)) {
                differ.diff(baselineCandidate.getName());
            }
            LOGGER.info("Approve current version? (y/n)");
            if (userAcceptsRequest(scanner)) {
                approveFile(baselineCandidate);
            }
        }
        scanner.close();
    }

    private void copyToBaseline(ApprovableFile toApprove, ApprovableFile baseline) throws IOException {
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
