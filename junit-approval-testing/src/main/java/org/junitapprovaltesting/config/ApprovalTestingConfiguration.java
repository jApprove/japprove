package org.junitapprovaltesting.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.verifier.StringVerifier;

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
    private String baselineDirectory;
    private String toApproveDirectory;
    private String diffTool;


    public ApprovalTestingConfiguration() {
        loadProperties();
    }

    private static final Logger LOGGER = LogManager.getLogger(ApprovalTestingConfiguration.class);

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
            LOGGER.info("Loading properties from: " + APPROVAL_TESTING_PROPERTIES);
        } catch (IOException e) {
            baselineDirectory = DEFAULT_BASELINE_DIRECTORY;
            toApproveDirectory = DEFAULT_TO_APPROVE_DIRECTORY;
            diffTool = DEFAULT_IDEA_DIFF;
            LOGGER.info("Using default properties");
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

}
