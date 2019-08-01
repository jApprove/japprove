package org.junitapprovaltesting.verifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junitapprovaltesting.errors.VerificationFailedError;
import org.junitapprovaltesting.errors.VersionNotApprovedError;
import org.junitapprovaltesting.model.JsonFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonVerifier extends Verifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonVerifier.class);
    private List<String> ignoredFields = new ArrayList<>();

    public JsonVerifier(String testName) {
        super(testName);
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
            if (!toApprove.equals(baseline, this.ignoredFields)) {
                LOGGER.info("Version not equal to approved version!");
                List<String> differences = toApprove.computeDifferences(baseline, this.ignoredFields);
                throw new VerificationFailedError(getErrorMessage(differences));
            }
            LOGGER.info("Version is equal to approved version.");
            toApprove.delete();
        } else {
            LOGGER.info("No approved version existing");
            throw new VersionNotApprovedError(toApprove);
        }
    }

    public JsonVerifier ignore(String path) {
        this.ignoredFields.add(path);
        return this;
    }
}
