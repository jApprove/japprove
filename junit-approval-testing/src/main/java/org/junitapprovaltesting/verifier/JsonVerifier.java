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

    private static final String REMOVE = "\"remove\"";
    private static final String ADD = "\"add\"";
    private static final String COPY = "\"copy\"";
    private static final String MOVE = "\"move\"";
    private static final String PATH = "path";
    private static final String OPERATION = "op";
    private static final String FROM = "from";

    private static final Logger LOGGER = LogManager.getLogger(JsonVerifier.class);
    private List<String> ignoredFields = new ArrayList<>();
    private JsonNode baseline;

    public JsonVerifier(String baselineName) {
        super(baselineName);
        try {
            baseline = fileService.getJsonBaseline(baselineName).readData();
        } catch (FileNotFoundException e) {
            baseline = null;
        }
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
        LOGGER.info("Starting new approval test with baseline: " + baselineName);
        if (baseline == null) {
            LOGGER.info("No approved version found");
            LOGGER.info("Creating new unapproved text file");
            fileService.createUnapprovedFile(data, baselineName);
            throw new VersionNotApprovedError(baselineName);
        }
        JsonNode dataWithoutIgnoredFields = removeIgnoredFields(data);
        JsonNode baselineWithoutIgnoredFields = removeIgnoredFields(baseline);
        if (!baselineWithoutIgnoredFields.equals(dataWithoutIgnoredFields)) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new unapproved text file");
            List<String> differences = getDifferences(baselineWithoutIgnoredFields, dataWithoutIgnoredFields);
            fileService.createUnapprovedFile(data, baselineName);
            throw new VerificationFailedError(formatDifferences(differences));
        }
        LOGGER.info("Current version is equal to approved version");
        fileService.removeUnapprovedFile(baselineName);
    }

    /**
     * Receives a JSONPath that should be ignored on the verification process.
     *
     * @param path a JSONPath that should be ignored on the verification process
     * @return the JsonVerifier
     */
    public JsonVerifier ignore(String path) {
        LOGGER.info("Ignoring Json element in approval test: " + path);
        ignoredFields.add(path);
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
        for (String pathElement : path.replace("\"", "").substring(1).split("/")) {
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
