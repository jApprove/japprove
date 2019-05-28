package org.junitapprovaltesting;


import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The {@link Approver} is able to approve data by comparing the data with a baseline.
 */
public class Approver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Approver.class);

    private static final String KDIFF = "C:\\Program Files (x86)\\KDiff3\\kdiff3";
    private static final String BASELINE = "baseline.txt";
    private static final String TO_APPROVE = "toApprove.txt";

    /**
     * Approve a list of strings by comparing the data with a baseline.
     *
     * @param data a list of strings
     */
    public void approve(List<String> data) {

        TextFile baseline = this.createTextFile(BASELINE);
        TextFile toApprove = this.createTextFile(TO_APPROVE);

        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (!toApprove.equals(baseline)) {
                this.callDiffer(toApprove, baseline);

                LOGGER.info("Approve? (y/n)");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.next();
                scanner.close();

                if (input.equals("y")) {
                    try {
                        baseline.writeData(data);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Assertions.fail("Not approved");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                toApprove.cleanUp();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void callDiffer(File toApprove, File baseline) {
        String cmd = KDIFF + " " + toApprove.getPath() + " " + baseline.getPath();
        LOGGER.info("Executing command: " + cmd);
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TextFile createTextFile(String path) {
        TextFile textFile = new TextFile(path);
        try {
            if (textFile.createNewFile()) {
                LOGGER.info("Created TextFile " + path);
            } else {
                LOGGER.info("Use existing TextFile " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textFile;
    }
}
