package org.junitapprovaltesting.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.exceptions.errors.VerificationFailedError;
import org.junitapprovaltesting.exceptions.errors.VersionNotApprovedError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code JsonVerifier} provides a method to verify JSON objects within Approval Tests. With its ignore method it
 * is also possible to ignore specific elements of a JSON object.
 */
public class JsonVerifier extends Verifier {

    private static final String BACKSLASH = "\"";
    private static final String EMPTY_STRING = "";
    private static final String SLASH = "/";
    private static final String REMOVE = "\"remove\"";
    private static final String ADD = "\"add\"";
    private static final String COPY = "\"copy\"";
    private static final String MOVE = "\"move\"";
    private static final String PATH = "path";
    private static final String OPERATION = "op";
    private static final String FROM = "from";
    private static final Logger LOGGER = LogManager.getLogger(JsonVerifier.class);
    private List<String> ignoredFields = new ArrayList<>();
    private JsonNode baselineData;

    public JsonVerifier(String baseline) {
        super(baseline);
        try {
            baselineData = baselineRepository.getJsonBaseline(baseline).readData();
        } catch (FileNotFoundException e) {
            baselineData = null;
        }
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
        LOGGER.info("Starting new approval test with baseline: " + baseline);
        if (baselineData == null) {
            LOGGER.info("No approved version found");
            LOGGER.info("Creating new baseline candidate");
            baselineRepository.createBaselineCandidate(data, baseline);
            throw new VersionNotApprovedError(baseline);
        }
        JsonNode dataWithoutIgnoredFields = removeIgnoredFields(data);
        JsonNode baselineWithoutIgnoredFields = removeIgnoredFields(baselineData);
        if (!baselineWithoutIgnoredFields.equals(dataWithoutIgnoredFields)) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new baseline candidate");
            List<String> differences = getDifferences(baselineWithoutIgnoredFields, dataWithoutIgnoredFields);
            baselineRepository.createBaselineCandidate(data, baseline);
            throw new VerificationFailedError(formatDifferences(differences));
        }
        LOGGER.info("Current version is equal to approved version");
        baselineRepository.removeBaselineCandidate(baseline);
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

    private List<String> getDifferences(JsonNode original, JsonNode revised) {
        List<String> differences = new ArrayList<>();
        JsonNode changes = JsonDiff.asJson(original, revised);
        for (JsonNode change : changes) {
            differences.add(visualizeChange(change, revised, original));
        }
        return differences;
    }

    private String visualizeChange(JsonNode change, JsonNode revised, JsonNode original) {
        StringBuilder builder = new StringBuilder();
        String path = change.get(PATH).toString();
        String operation = change.get(OPERATION).toString();
        JsonNode newElement = getLeafOfJsonNode(path, revised);
        JsonNode oldElement = getLeafOfJsonNode(path, original);
        builder.append("Operation: " + operation + "\n");
        builder.append("Path: " + path + "\n");
        if (operation.equals(REMOVE)) {
            builder.append("--- " + oldElement + " \n");
        } else
            if (operation.equals(ADD) || operation.equals(COPY)) {
                builder.append("+++ " + newElement + " \n");
            } else
                if (operation.equals(MOVE)) {
                    builder.append("From " + change.get(FROM).toString() + " \n");
                    builder.append("To: " + path + " \n");
                    builder.append("+++ " + newElement + " \n");
                } else {
                    builder.append("+++ " + newElement + " \n");
                    builder.append("--- " + oldElement + " \n");
                }
        return builder.toString();
    }

    private JsonNode getLeafOfJsonNode(String path, JsonNode jsonNode) {
        for (String pathElement : path.replace(BACKSLASH, EMPTY_STRING).substring(1).split(SLASH)) {
            if (isNumeric(pathElement)) {
                jsonNode = jsonNode.get(Integer.parseInt(pathElement));
            } else {
                jsonNode = jsonNode.get(pathElement);
            }
        }
        return jsonNode;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
