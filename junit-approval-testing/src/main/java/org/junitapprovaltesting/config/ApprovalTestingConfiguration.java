package org.junitapprovaltesting.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A configuration class for approval testing. The information is read from the approvaltesting.properties file in
 * the resources directory.
 */
public class ApprovalTestingConfiguration {

    private static final String APPROVAL_TESTING_PROPERTIES = "src" + File.separator + "test" + File.separator +
            "resources" + File.separator + "approvaltesting.properties";
    private static final String DEFAULT_IDEA_DIFF =
            "C:" + File.separator + "Program Files" + File.separator + "JetBrains" + File.separator +
                    "IntelliJ IDEA Community Edition 2019.1.3" + File.separator + "bin" + File.separator +
                    "idea64 diff";
    private static final String DEFAULT_BASELINE_DIRECTORY = "baselines" + File.separator;
    private static final String DEFAULT_TO_APPROVE_DIRECTORY = "build" + File.separator + "approvals" + File.separator;
    private static final String DEFAULT_TO_APPROVE_EXTENSION = "_toApprove";
    private static final String DEFAULT_FILE_ENDING = "txt";
    private String baselineDirectory;
    private String toApproveDirectory;
    private String diffTool;
    private String fileEnding;
    private String toApproveExtension;

    public ApprovalTestingConfiguration() {
        loadProperties();
    }

    private void loadProperties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(APPROVAL_TESTING_PROPERTIES));
            if (prop.getProperty("baselineDirectory") == null) {
                baselineDirectory = DEFAULT_BASELINE_DIRECTORY;
            } else {
                baselineDirectory = prop.getProperty("baselineDirectory").replace("/", File.separator)
                                        .replace("\\", File.separator);
            }
            if (prop.getProperty("toApproveDirectory") == null) {
                toApproveDirectory = DEFAULT_TO_APPROVE_DIRECTORY;
            } else {
                toApproveDirectory = prop.getProperty("toApproveDirectory").replace("\\", File.separator)
                                         .replace("/", File.separator);
            }
            if (prop.getProperty("diffTool") == null) {
                diffTool = DEFAULT_IDEA_DIFF;
            } else {
                diffTool = prop.getProperty("diffTool").replace("\\", File.separator).replace("/", File.separator);
            }
            if (prop.getProperty("fileEnding") == null) {
                fileEnding = "." + DEFAULT_FILE_ENDING;
            } else {
                fileEnding = "." + prop.getProperty("fileEnding");
            }
            if (prop.getProperty("toApproveExtension") == null) {
                toApproveExtension = DEFAULT_TO_APPROVE_EXTENSION;
            } else {
                toApproveExtension = prop.getProperty("toApproveExtension");
            }
        } catch (IOException e) {
            baselineDirectory = DEFAULT_BASELINE_DIRECTORY;
            toApproveDirectory = DEFAULT_TO_APPROVE_DIRECTORY;
            diffTool = DEFAULT_IDEA_DIFF;
            fileEnding = "." + DEFAULT_FILE_ENDING;
            toApproveExtension = DEFAULT_TO_APPROVE_EXTENSION;
        }
    }

    /**
     * Returns the path to the baseline directory.
     *
     * @return the path to the baseline directory.
     */
    public String getBaselineDirectory() {
        return baselineDirectory;
    }

    /**
     * Returns the path to the toApprove directory.
     *
     * @return the path to the toApprove directory.
     */
    public String getToApproveDirectory() {
        return toApproveDirectory;
    }

    /**
     * Returns the path to the diff tool.
     *
     * @return the path to the diff tool.
     */
    public String getDiffTool() {
        return diffTool;
    }

    /**
     * Returns the ending of the stored files.
     *
     * @return the ending of the stored files.
     */
    public String getFileEnding() {
        return fileEnding;
    }

    /**
     * Returns the toApprove extension.
     *
     * @return the toApprove extension.
     */
    public String getToApproveExtension() {
        return toApproveExtension;
    }
}
