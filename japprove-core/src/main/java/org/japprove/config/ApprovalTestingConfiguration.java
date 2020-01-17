package org.japprove.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A configuration class for approval testing. The information is read from the japprove.properties
 * file in the resources directory.
 */
public class ApprovalTestingConfiguration {

    private static final String APPROVAL_TESTING_PROPERTIES = "src"
            + File.separator
            + "test"
            + File.separator
            + "resources"
            + File.separator
            + "japprove.properties";
    private static final String DEFAULT_IDEA_DIFF = "C:"
            + File.separator
            + "Program Files"
            + File.separator
            + "JetBrains"
            + File.separator
            + "IntelliJ IDEA Community Edition 2019.2.3"
            + File.separator
            + "bin"
            + File.separator
            + "idea64 diff";
    private static final String DEFAULT_BASELINE_DIRECTORY = "baselines" + File.separator;
    private static final String DEFAULT_BASELINE_CANDIDATE_DIRECTORY = "build"
            + File.separator
            + "baselineCandidates"
            + File.separator;
    private static final Logger LOGGER = LogManager.getLogger(ApprovalTestingConfiguration.class);
    private String baselineDirectory;
    private String baselineCandidateDirectory;
    private String diffTool;

    public ApprovalTestingConfiguration() {
        loadProperties();
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
     * Returns the path to the baselineCandidate directory.
     *
     * @return the path to the baselineCandidate directory.
     */
    public String getBaselineCandidateDirectory() {
        return baselineCandidateDirectory;
    }

    /**
     * Returns the path to the diff tool.
     *
     * @return the path to the diff tool.
     */
    public String getDiffTool() {
        return diffTool;
    }

    private void loadProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(APPROVAL_TESTING_PROPERTIES));
            if (props.getProperty("baselineDirectory") == null) {
                baselineDirectory = DEFAULT_BASELINE_DIRECTORY;
            } else {
                String rawPath = props.getProperty("baselineDirectory");
                baselineDirectory = formatPath(rawPath);
            }
            if (props.getProperty("toApproveDirectory") == null) {
                baselineCandidateDirectory = DEFAULT_BASELINE_CANDIDATE_DIRECTORY;
            } else {
                baselineCandidateDirectory =
                        formatPath(props.getProperty("baselineCandidateDirectory"));
            }
            if (props.getProperty("diffTool") == null) {
                diffTool = DEFAULT_IDEA_DIFF;
            } else {
                diffTool = adaptFileSeparatorsToOperatingSystem(props.getProperty("diffTool"));
            }
            LOGGER.info("Loading properties from: " + APPROVAL_TESTING_PROPERTIES);
        } catch (IOException e) {
            baselineDirectory = DEFAULT_BASELINE_DIRECTORY;
            baselineCandidateDirectory = DEFAULT_BASELINE_CANDIDATE_DIRECTORY;
            diffTool = DEFAULT_IDEA_DIFF;
            LOGGER.info("Using default properties");
        }
    }

    private String formatPath(String path) {
        String formattedPath;
        formattedPath = adaptFileSeparatorsToOperatingSystem(path);
        formattedPath = ensureTrailingFileSeparatorExists(formattedPath);
        return formattedPath;
    }

    private String adaptFileSeparatorsToOperatingSystem(String path) {
        return path.replace("\\", File.separator).replace("/", File.separator);
    }

    private String ensureTrailingFileSeparatorExists(String path) {
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        return path;
    }
}
