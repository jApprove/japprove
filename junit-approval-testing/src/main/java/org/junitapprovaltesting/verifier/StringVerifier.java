package org.junitapprovaltesting.verifier;

import org.junitapprovaltesting.errors.VerificationFailedError;
import org.junitapprovaltesting.errors.VersionNotApprovedError;
import org.junitapprovaltesting.model.TextFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class StringVerifier extends Verifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringVerifier.class);

    public StringVerifier(String testName) {
        super(testName);
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
            LOGGER.info("Version is equal to approved version.");
            toApprove.delete();
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
            LOGGER.info("Version is equal to approved version. Deleting " + toApprove);
            toApprove.delete();
        } else {
            LOGGER.info("No approved version existing");
            throw new VersionNotApprovedError(toApprove);
        }
    }
}
