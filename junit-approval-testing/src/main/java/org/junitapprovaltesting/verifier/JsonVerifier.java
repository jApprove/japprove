package org.junitapprovaltesting.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.engine.ApprovalTestingEngine;
import org.junitapprovaltesting.exceptions.BaselineCandidateCreationFailedException;
import org.junitapprovaltesting.exceptions.BaselineNotFoundException;
import org.junitapprovaltesting.exceptions.VerificationFailedException;
import org.junitapprovaltesting.exceptions.errors.VerificationFailedError;
import org.junitapprovaltesting.exceptions.errors.VersionNotApprovedError;

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

    public JsonVerifier(ApprovalTestingEngine approvalTestingEngine) {
        super(approvalTestingEngine);
    }

    /**
     * Receives a JsonNode that should be verified within an Approval Test.
     * <p>
     * Within the verification process, a baseline candidate is created in the baseline candidate's directory and
     * compared to a {@code baseline} (if exists). In the case the versions are equal, the test passes. If no
     * baseline exists, a {@code VersionNotApprovedError} is thrown. If there is a baseline that is not equal to the
     * current version, a {@code VerificationFailedError} is thrown.
     *
     * @param data The JsonNode that should be verified
     */
    public void verify(JsonNode data) {
        LOGGER.info("Starting new approval test with baseline: " + baselineName);
        JsonNode baselineData;
        try {
            baselineData = baselineRepository.getContentOfJsonBaseline(baselineName);
        } catch (BaselineNotFoundException e) {
            LOGGER.info("No approved version found");
            LOGGER.info("Creating new baseline candidate");
            createBaselineCandidate(data);
            throw new VersionNotApprovedError(baselineName);
        }
        JsonNode dataWithoutIgnoredFields = removeIgnoredFields(data);
        JsonNode baselineWithoutIgnoredFields = removeIgnoredFields(baselineData);
        if (!baselineWithoutIgnoredFields.equals(dataWithoutIgnoredFields)) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new baseline candidate");
            String differences = differ.getDifferences(baselineWithoutIgnoredFields, dataWithoutIgnoredFields);
            createBaselineCandidate(data);
            throw new VerificationFailedError(differences);
        }
        LOGGER.info("Current version is equal to approved version");
        baselineRepository.removeBaselineCandidate(baselineName);
    }

    /**
     * Receives a JSONPath that should be ignored on the verification process.
     *
     * @param jsonPath a JSONPath that should be ignored on the verification process
     * @return the JsonVerifier
     */
    public JsonVerifier ignore(String jsonPath) {
        LOGGER.info("Ignoring Json element in approval test: " + jsonPath);
        ignoredFields.add(jsonPath);
        return this;
    }

    private void createBaselineCandidate(JsonNode data) {
        try {
            baselineRepository.createBaselineCandidate(data, baselineName);
        } catch (BaselineCandidateCreationFailedException e) {
            throw new VerificationFailedException("Internal error while creating baseline");
        }
    }

    private JsonNode removeIgnoredFields(JsonNode jsonToApprove) {
        DocumentContext jsonContext = JsonPath.parse(jsonToApprove.toString());
        for (String ignoredField : ignoredFields) {
            jsonToApprove = stringToJson(jsonContext.delete(ignoredField).jsonString());
        }
        return jsonToApprove;
    }

    private JsonNode stringToJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading Json String");
        }
    }

}
