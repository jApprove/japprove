package org.junitapprovaltesting;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junitapprovaltesting.errors.VerificationFailedError;
import org.junitapprovaltesting.errors.VersionNotApprovedError;
import org.junitapprovaltesting.model.JsonFile;
import org.junitapprovaltesting.model.TextFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * The {@link Approver} is able to approve data by comparing the data with a baseline.
 */
public class Approver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Approver.class);
    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_FILE = "_toApprove";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";
    private static final String TXT_ENDING = ".txt";
    private String baselineFileName;
    private String toApproveFileName;

    public Approver(String testName) {
        this.baselineFileName = BASELINE_DIRECTORY + testName + TXT_ENDING;
        this.toApproveFileName = TO_APPROVE_DIRECTORY + testName + TO_APPROVE_FILE + TXT_ENDING;
    }

    public void verify(String data) {
        LOGGER.info("Starting new approval test");
        TextFile toApprove = new TextFile(this.toApproveFileName);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found");
        }
        TextFile baseline = new TextFile(this.baselineFileName);
        if (baseline.exists()) {
            LOGGER.info("An approved version exists! Comparing...");
            try {
                if (!toApprove.equals(baseline)) {
                    LOGGER.info("Version not equal to approved version!");
                    List<String> differences = toApprove.computeDifferences(baseline);
                    throw new VerificationFailedError(getErrorMessage(differences));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while comparing files");
            }
        } else {
            LOGGER.info("No approved version existing");
            throw new VersionNotApprovedError(toApprove);
        }
    }

    public void verify(List<String> data) {
        LOGGER.info("Starting new approval test");
        TextFile toApprove = new TextFile(this.toApproveFileName);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found");
        }
        TextFile baseline = new TextFile(this.baselineFileName);
        if (baseline.exists()) {
            LOGGER.info("An approved version exists! Comparing...");
            try {
                if (!toApprove.equals(baseline)) {
                    LOGGER.info("Version not equal to approved version!");
                    List<String> differences = toApprove.computeDifferences(baseline);
                    throw new VerificationFailedError(getErrorMessage(differences));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while comparing files");
            }
        } else {
            LOGGER.info("No approved version existing");
            throw new VersionNotApprovedError(toApprove);
        }
    }

    public void verify(JsonNode data) {
        LOGGER.info("Starting new approval test");
        JsonFile toApprove = new JsonFile(this.toApproveFileName);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot process JSON file");
        }
        JsonFile baseline = new JsonFile(this.baselineFileName);
        if (baseline.exists()) {
            LOGGER.info("An approved version exists! Comparing...");
            try {
                if (!toApprove.equals(baseline)) {
                    LOGGER.info("Version not equal to approved version!");
                    List<String> differences = toApprove.computeDifferences(baseline);
                    throw new VerificationFailedError(getErrorMessage(differences));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while comparing files");
            }
        } else {
            LOGGER.info("No approved version existing");
            throw new VersionNotApprovedError(toApprove);
        }
    }

    private String getErrorMessage(List<String> differences) {
        StringBuilder builder = new StringBuilder();
        differences.forEach(builder::append);
        return builder.toString();
    }

}
