package org.junitapprovaltesting;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.apache.commons.io.FileUtils;
import org.junitapprovaltesting.errors.VerificationFailedError;
import org.junitapprovaltesting.errors.VersionNotApprovedError;
import org.junitapprovaltesting.model.TextFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void verify(String data) {
        LOGGER.info("Verifying data");
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
                    String differences = computeDiff(toApprove, baseline);
                    throw new VerificationFailedError(differences);
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
        LOGGER.info("Verifying data");
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
                    String differences = computeDiff(toApprove, baseline);
                    throw new VerificationFailedError(differences);
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
        LOGGER.info("Verifying data");
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
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot process JSON file");
        }
        TextFile baseline = new TextFile(this.baselineFileName);
        if (baseline.exists()) {
            LOGGER.info("An approved version exists! Comparing...");
            try {
                if (!toApprove.equals(baseline)) {
                    LOGGER.info("Version not equal to approved version!");
                    String differences = computeJSONDiff(toApprove, baseline);
                    throw new VerificationFailedError(differences);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while comparing files");
            }
        } else {
            LOGGER.info("No approved version existing");
            throw new VersionNotApprovedError(toApprove);
        }
    }

    private String computeDiff(TextFile toApprove, TextFile baseline) {
        LOGGER.info("Compute differences of " + toApprove.toPath() + " and " + baseline.toPath());
        List<String> original = null;
        List<String> revised = null;
        Patch<String> patch = null;
        try {
            original = Files.readAllLines(baseline.toPath());
            revised = Files.readAllLines(toApprove.toPath());
            patch = DiffUtils.diff(original, revised);
        } catch (IOException | DiffException e) {
            throw new RuntimeException();
        }
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff("Baseline", "toApprove", original, patch, 0);
        StringBuilder builder = new StringBuilder();
        for (String diff : unifiedDiff) {
            builder.append(diff);
            builder.append("\n");
        }
        return builder.toString();
    }

    private String computeJSONDiff(TextFile toApprove, TextFile baseline) {
        String diff = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonToApprove = objectMapper.readTree(FileUtils.readFileToString(toApprove, StandardCharsets.UTF_8));
            JsonNode jsonBaseline = objectMapper.readTree(FileUtils.readFileToString(baseline, StandardCharsets.UTF_8));
            JsonNode patch = JsonDiff.asJson(jsonBaseline, jsonToApprove);
            int i = 0;
            StringBuilder builder = new StringBuilder();
            for (JsonNode node : patch) {
                builder.append(++i + ":\n");
                String[] parts = node.get("path").toString().replace("\"", "").substring(1).split("/");
                JsonNode temp = jsonToApprove;
                JsonNode temp2 = jsonBaseline;
                for (String s : parts) {
                    if (isNumeric(s)) {
                        int index = Integer.parseInt(s);
                        temp = temp.get(index);
                        temp2 = temp2.get(index);
                    } else {
                        temp = temp.get(s);
                        temp2 = temp2.get(s);
                    }
                }
                builder.append("Operation: " + node.get("op") + "\n");
                builder.append("Path: " + node.get("path") + "\n");
                if (node.get("op").toString().equals("\"replace\"")) {
                    builder.append("+++ " + temp + " \n");
                    builder.append("--- " + temp2 + " \n");
                } else if (node.get("op").toString().equals("\"remove\"")) {
                    builder.append("--- " + temp2 + " \n");
                } else if (node.get("op").toString().equals("\"add\"")) {
                    builder.append("+++ " + temp + " \n");
                } else if (node.get("op").toString().equals("\"copy\"")) {
                    builder.append("+++ " + temp + " \n");
                } else if (node.get("op").toString().equals("\"move\"")) {
                    builder.append("From " + node.get("from") + " \n");
                    builder.append("To: " + node.get("path") + " \n");
                    builder.append("+++ " + temp + " \n");
                } else {
                    builder.append("+++ " + temp + " \n");
                    builder.append("--- " + temp2 + " \n");
                }
                builder.append("\n");
            }
            diff = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diff;
    }

}
