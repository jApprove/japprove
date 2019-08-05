package org.junitapprovaltesting.verifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.errors.VerificationFailedError;
import org.junitapprovaltesting.errors.VersionNotApprovedError;
import org.junitapprovaltesting.model.JsonFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code JsonVerifier} provides a method to verify JSON objects within Approval Tests. With its ignore method it
 * is also possible to ignore specific elements of a JSON object.
 */
public class JsonVerifier extends Verifier {

    private static final Logger LOGGER = LogManager.getLogger(JsonVerifier.class);
    private List<String> ignoredFields = new ArrayList<>();

    public JsonVerifier(String testName) {
        super(testName);
    }

    /**
     * Receives a JsonNode that should be verified within an Approval Test.
     * <p>
     * Within the verification process, a {@code toApprove} toApprove file is created in the build directory and
     * compared to a {@code baseline} file (if exists). In the case the versions are equal, the test passes. If no
     * baseline exists, a {@code VersionNotApprovedError} is thrown. If there is a baseline that is not equal to the
     * current version, a {@code VerificationFailedError} is thrown.
     *
     * @param data The JsonNode that should be verified
     */
    public void verify(JsonNode data) {
        LOGGER.info("Starting new approval test: " + testName);
        JsonFile toApprove = new JsonFile(TO_APPROVE_DIRECTORY + testName + TO_APPROVE_FILE + TXT_ENDING);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot process JSON file");
        }
        JsonFile baseline = new JsonFile(BASELINE_DIRECTORY + testName + TXT_ENDING);
        if (baseline.exists()) {
            LOGGER.info("An approved version exists. Comparing...");
            if (!toApprove.equals(baseline, ignoredFields)) {
                LOGGER.info("Current version is not equal to approved version.");
                List<String> differences = toApprove.computeDifferences(baseline, ignoredFields);
                throw new VerificationFailedError(formatDifferences(differences));
            }
            LOGGER.info("Current version is equal to approved version.");
            toApprove.delete();
        } else {
            LOGGER.info("No approved version exists.");
            throw new VersionNotApprovedError(toApprove);
        }
    }

    /**
     * Receives a JSONPath that should be ignored on the verification process.
     *
     * @param path a JSONPath that should be ignored on the verification process
     * @return the JsonVerifier
     */
    public JsonVerifier ignore(String path) {
        this.ignoredFields.add(path);
        return this;
    }
}
