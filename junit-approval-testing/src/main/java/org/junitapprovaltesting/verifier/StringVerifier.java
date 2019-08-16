package org.junitapprovaltesting.verifier;


import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.exceptions.errors.VerificationFailedError;
import org.junitapprovaltesting.exceptions.errors.VersionNotApprovedError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code StringVerifier} provides methods to verify String objects or String lists within Approval Tests.
 */
public class StringVerifier extends Verifier {

    private static final Logger LOGGER = LogManager.getLogger(StringVerifier.class);
    private String baselineData;

    public StringVerifier(String baseline) {
        super(baseline);
        try {
            baselineData = baselineRepository.getTextBaseline(baseline).readData();
        } catch (FileNotFoundException e) {
            baselineData = null;
        } catch (IOException e) {
            baselineData = null;
        }
    }

    /**
     * Receives a String that should be verified within an Approval Test.
     * <p>
     * Within the verification process, the passed data is compared to the data in the corresponding baseline. In the
     * case the versions are equal, the test passes. If no baseline exists, a {@code VersionNotApprovedError} is
     * thrown. If there is a baseline that is not equal to the current version, a {@code VerificationFailedError}
     * is thrown.
     *
     * @param data The String that should be verified
     */
    public void verify(String data) {
        LOGGER.info("Starting new approval test with baseline: " + baseline);
        if (this.baselineData == null) {
            LOGGER.info("No approved version found");
            LOGGER.info("Creating new baseline candidate");
            baselineRepository.createBaselineCandidate(data, baseline);
            throw new VersionNotApprovedError(baseline);
        }
        if (!this.baselineData.equals(data)) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new baseline candidate");
            List<String> differences = getDifferences(this.baselineData, data);
            baselineRepository.createBaselineCandidate(data, baseline);
            throw new VerificationFailedError(formatDifferences(differences));
        }
        LOGGER.info("Current version is equal to approved version");
        baselineRepository.removeBaselineCandidate(baseline);
    }

    /**
     * Receives a List of Strings that should be verified within an Approval Test.
     * <p>
     * Within the verification process, the passed data is compared to the data in the corresponding baseline. In the
     * case the versions are equal, the test passes. If no baseline exists, a {@code VersionNotApprovedError} is
     * thrown. If there is a baseline that is not equal to the current version, a {@code VerificationFailedError}
     * is thrown.
     *
     * @param data The String that should be verified
     */
    public void verify(List<String> data) {
        verify(String.join("\n", data));
    }

    private List<String> getDifferences(String original, String revised) {
        try {
            Patch<String> patch = DiffUtils.diff(Arrays.asList(original), Arrays.asList(revised));
            return UnifiedDiffUtils
                    .generateUnifiedDiff("Baseline", "Baseline Candidate", Arrays.asList(original), patch, 0);
        } catch (DiffException e) {
            throw new RuntimeException("Cannot compute differences! " + e);
        }
    }
}
