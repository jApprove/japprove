package org.junitapprovaltesting.verifier;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.errors.VerificationFailedError;
import org.junitapprovaltesting.errors.VersionNotApprovedError;
import org.junitapprovaltesting.model.TextFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * The {@code StringVerifier} provides a method to verify String objects within Approval Tests.
 */
public class StringVerifier extends Verifier {

    private static final Logger LOGGER = LogManager.getLogger(StringVerifier.class);

    public StringVerifier(String testName) {
        super(testName);
    }

    /**
     * Receives a String that should be verified within an Approval Test.
     * <p>
     * Within the verification process, a {@code toApprove} toApprove file is created in the build directory and
     * compared to a {@code baseline} file (if exists). In the case the versions are equal, the test passes. If no
     * baseline exists, a {@code VersionNotApprovedError} is thrown. If there is a baseline that is not equal to the
     * current version, a {@code VerificationFailedError} is thrown.
     *
     * @param data The String that should be verified
     */
    public void verify(String data) {
        LOGGER.info("Starting new approval test: " + testName);
        TextFile toApprove = new TextFile(TO_APPROVE_DIRECTORY + testName + TO_APPROVE_FILE + TXT_ENDING);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found.");
        }
        TextFile baseline = new TextFile(BASELINE_DIRECTORY + testName + TXT_ENDING);
        if (baseline.exists()) {
            LOGGER.info("An approved version exists. Comparing...");
            try {
                if (!toApprove.equals(baseline)) {
                    LOGGER.info("Current version is not equal to approved version.");
                    List<String> differences = toApprove.computeDifferences(baseline);
                    throw new VerificationFailedError(toApprove, formatDifferences(differences));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while comparing files.");
            }
            LOGGER.info("Current version is equal to approved version.");
            toApprove.delete();
        } else {
            LOGGER.info("No approved version exists.");
            throw new VersionNotApprovedError(toApprove);
        }
    }

    /**
     * Receives a list of Strings that should be verified within an Approval Test.
     * <p>
     * Within the verification process, a {@code toApprove} toApprove file is created in the build directory and
     * compared to a {@code baseline} file (if exists). In the case the versions are equal, the test passes. If no
     * baseline exists, a {@code VersionNotApprovedError} is thrown. If there is a baseline that is not equal to the
     * current version, a {@code VerificationFailedError} is thrown.
     *
     * @param data The list of Strings that should be verified
     */
    public void verify(List<String> data) {
        LOGGER.info("Starting new approval test: " + testName);
        TextFile toApprove = new TextFile(TO_APPROVE_DIRECTORY + testName + TO_APPROVE_FILE + TXT_ENDING);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found.");
        }
        TextFile baseline = new TextFile(BASELINE_DIRECTORY + testName + TXT_ENDING);
        if (baseline.exists()) {
            LOGGER.info("An approved version exists. Comparing...");
            try {
                if (!toApprove.equals(baseline)) {
                    LOGGER.info("Current version is not equal to approved version.");
                    List<String> differences = toApprove.computeDifferences(baseline);
                    throw new VerificationFailedError(toApprove, formatDifferences(differences));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while comparing files.");
            }
            LOGGER.info("Current version is equal to approved version.");
            toApprove.delete();
        } else {
            LOGGER.info("No approved version exists.");
            throw new VersionNotApprovedError(toApprove);
        }
    }
}
