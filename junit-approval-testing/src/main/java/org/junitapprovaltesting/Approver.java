package org.junitapprovaltesting;


import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.junitapprovaltesting.errors.VerificationFailedError;
import org.junitapprovaltesting.model.TextFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * Approve a list of strings by comparing the data with a baseline.
     *
     * @param data a list of strings
     */
    public void verify(List<String> data) {

        this.createDirectories();

        TextFile baseline = this.createTextFile(baselineFileName);
        TextFile toApprove = this.createTextFile(toApproveFileName);

        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }

        try {
            if (!toApprove.equals(baseline)) {

                List<String> original = Files.readAllLines(baseline.toPath());
                List<String> revised = Files.readAllLines(toApprove.toPath());

                Patch<String> patch = DiffUtils.diff(original, revised);

                List<String> unifiedDiff =
                        UnifiedDiffUtils.generateUnifiedDiff("Baseline", "toApprove", original, patch, 0);
                StringBuilder builder = new StringBuilder();

                for (String diff : unifiedDiff) {
                    builder.append(diff);
                    builder.append("\n");
                }
                throw new VerificationFailedError(builder.toString());
            } else {
                if (toApprove.delete()) {
                    LOGGER.info("Delete " + toApprove.getPath());
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new VerificationFailedError(e.getMessage());
        } catch (DiffException e) {
            e.printStackTrace();
        }
    }

    private void createDirectories() {
        File baselineDirectory = new File(BASELINE_DIRECTORY);
        baselineDirectory.mkdirs();
        File toApproveDirectory = new File(TO_APPROVE_DIRECTORY);
        toApproveDirectory.mkdirs();
    }

    private TextFile createTextFile(String path) {
        TextFile textFile = new TextFile(path);
        try {
            if (textFile.createNewFile()) {
                LOGGER.info("Create " + path);
            } else {
                LOGGER.info("Use existing " + path);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new VerificationFailedError(e.getMessage());
        }
        return textFile;
    }

}
