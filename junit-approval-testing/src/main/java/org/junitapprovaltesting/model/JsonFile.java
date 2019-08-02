package org.junitapprovaltesting.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonFile extends TextFile {

    public static final String REMOVE = "\"remove\"";
    public static final String ADD = "\"add\"";
    public static final String COPY = "\"copy\"";
    public static final String MOVE = "\"move\"";
    public static final String PATH = "path";
    public static final String OPERATION = "op";
    public static final String FROM = "from";

    public JsonFile(String path) {
        super(path);
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Stores a JsonNode in the JsonFile
     *
     * @param data the JsonNode that should be stored
     * @throws FileNotFoundException thrown if the file not exists
     */
    public void writeData(JsonNode data) throws FileNotFoundException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = new PrintWriter(this);
        out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
        out.close();
    }

    /**
     * Computes the differences of this and another JsonFile.
     *
     * @param other the file this file should be compared to
     * @return a list of the differences
     */
    public List<String> computeDifferences(JsonFile other, List<String> ignoredFields) {
        List<String> differences = new ArrayList<>();
        JsonNode jsonToApprove = readFileToJson(this);
        jsonToApprove = removeIgnoredFields(jsonToApprove, ignoredFields);
        JsonNode jsonBaseline = readFileToJson(other);
        jsonBaseline = removeIgnoredFields(jsonBaseline, ignoredFields);

        JsonNode changes = JsonDiff.asJson(jsonBaseline, jsonToApprove);
        for (JsonNode change : changes) {
            differences.add(visualizeChange(change, jsonToApprove, jsonBaseline));
        }
        return differences;
    }

    private JsonNode removeIgnoredFields(JsonNode jsonToApprove, List<String> ignoredFields) {
        DocumentContext jsonContext = JsonPath.parse(jsonToApprove.toString());
        for (String ignoredField : ignoredFields) {
            jsonToApprove = readStringToJson(jsonContext.delete(ignoredField).jsonString());
        }
        return jsonToApprove;
    }

    private JsonNode readStringToJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading Json String");
        }
    }

    private JsonNode readFileToJson(JsonFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Error while reading Json file");
        }
    }

    private String visualizeChange(JsonNode change, JsonNode jsonToApprove, JsonNode jsonBaseline) {
        StringBuilder builder = new StringBuilder();
        String path = change.get(PATH).toString();
        String operation = change.get(OPERATION).toString();
        JsonNode newElement = getLeafOfJsonNode(path, jsonToApprove);
        JsonNode oldElement = getLeafOfJsonNode(path, jsonBaseline);
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

    /**
     * Checks if two JsonFiles are equal
     *
     * @param other the file this file should be compared to
     * @return true if the files are equal, false otherwise
     * @throws IOException
     */
    public boolean equals(JsonFile other, List<String> ignoredFields) {
        return this.computeDifferences(other, ignoredFields).isEmpty();
    }
}
