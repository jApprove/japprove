package org.japproval.differ;

import com.fasterxml.jackson.databind.JsonNode;
import com.flipkart.zjsonpatch.JsonDiff;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.japproval.engine.ApprovalTestingEngine;
import org.japproval.exceptions.DiffingFailedException;
import org.japproval.repositories.BaselineRepositoryImpl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A central class that is responsible for computing differences of two strings or json nodes. It is also possible to
 * call an external diff tool.
 */
public class Differ {

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
    private BaselineRepositoryImpl baselineRepository;
    private String pathToDiffTool;

    public Differ(ApprovalTestingEngine approvalTestingEngine, String pathToDiffTool) {
        this.baselineRepository = (BaselineRepositoryImpl) approvalTestingEngine.getBaselineRepository();
        this.pathToDiffTool = pathToDiffTool;
    }

    /**
     * Computes the differences of the baseline candidate and the baseline by calling an external diff tool.
     *
     * @param baselineCandidateName the name of the baseline candidate for which the differences should be computed
     */
    public void callExternalDiffTool(String baselineCandidateName) {
        File baselineCandidate;
        try {
            baselineCandidate = baselineRepository.getBaselineCandidateAsFile(baselineCandidateName);
        } catch (IOException e) {
            throw new DiffingFailedException("Baseline candidate " + baselineCandidateName + " not found!");
        }
        File baseline;
        try {
            baseline = baselineRepository.getBaselineAsFile(baselineCandidateName);
        } catch (IOException e) {
            throw new DiffingFailedException("Baseline " + baselineCandidateName + " not found!");
        }
        String cmd = pathToDiffTool + " " + baselineCandidate.getPath() + " " + baseline.getPath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new DiffingFailedException("Diff tool " + pathToDiffTool + " not found!");
        }
    }

    /**
     * Computes the differences for two passed strings.
     *
     * @param original the original string
     * @param revised  the changed string
     * @return a formatted string with the differences
     */
    public String getDifferences(String original, String revised) {
        try {
            Patch<String> patch = DiffUtils.diff(Arrays.asList(original), Arrays.asList(revised));
            List<String> differences = UnifiedDiffUtils
                    .generateUnifiedDiff("Baseline", "Baseline Candidate", Arrays.asList(original), patch, 0);
            return formatDifferences(differences);
        } catch (DiffException e) {
            throw new RuntimeException("Cannot compute differences! " + e);
        }
    }

    /**
     * Computes the differences for two passed json nodes.
     *
     * @param original the original json node
     * @param revised  the changed json node
     * @return a formatted string with the differences
     */
    public String getDifferences(JsonNode original, JsonNode revised) {
        StringBuilder differences = new StringBuilder();
        JsonNode changes = JsonDiff.asJson(original, revised);
        for (JsonNode change : changes) {
            differences.append(visualizeChange(change, revised, original));
            differences.append("\n");
        }
        return differences.toString();
    }

    private String formatDifferences(List<String> differences) {
        StringBuilder builder = new StringBuilder();
        for (String difference : differences) {
            builder.append(difference);
            builder.append("\n");
        }
        return builder.toString();
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
