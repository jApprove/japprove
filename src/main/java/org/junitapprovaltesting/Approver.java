package org.junitapprovaltesting;


import org.junit.jupiter.api.Assertions;
import org.junitapprovaltesting.model.TextFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * The {@link Approver} is able to approve data by comparing the data with a baseline.
 */
public class Approver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Approver.class);
    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2018.3.3\\bin\\idea64 diff";
    private static final String BASELINE_FILE = "baseline";
    private static final String BASELINE_DIRECTORY = "baselines\\";
    private static final String TO_APPROVE_FILE = "toApprove";
    private static final String TO_APPROVE_DIRECTORY = "build\\approvals\\";
    private static final String TXT_ENDING = ".txt";
    private String baselineFileName;
    private String toApproveFileName;

    public Approver(String testName) {
        this.baselineFileName = BASELINE_DIRECTORY + BASELINE_FILE + testName + TXT_ENDING;
        this.toApproveFileName = TO_APPROVE_DIRECTORY + TO_APPROVE_FILE + testName + TXT_ENDING;
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
                Assertions.fail("Found differences");
            } else {
                if (toApprove.delete()) {
                    LOGGER.info("Delete " + toApprove.getPath());
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            Assertions.fail(e.getMessage());
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
            Assertions.fail(e.getMessage());
        }
        return textFile;
    }

}
