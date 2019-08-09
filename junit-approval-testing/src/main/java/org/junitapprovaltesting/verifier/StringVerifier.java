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
    private List<String> data;

    public StringVerifier(String baselineName) {
        super(baselineName);
        try {
            data = fileService.getTextBaseline(baselineName).readData();
        } catch (FileNotFoundException e) {
            data = null;
        } catch (IOException e) {
            data = null;
        }
        fileService.removeUnapprovedFile(baselineName);
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
        LOGGER.info("Starting new approval test with baseline: " + baselineName);
        if (this.data == null) {
            LOGGER.info("No approved version found");
            LOGGER.info("Creating new unapproved text file");
            fileService.createUnapprovedFile(data, baselineName);
            throw new VersionNotApprovedError(baselineName);
        }
        if (!this.data.equals(Arrays.asList(data))) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new unapproved text file");
            List<String> differences = getDifferences(this.data, Arrays.asList(data));
            fileService.createUnapprovedFile(data, baselineName);
            throw new VerificationFailedError(formatDifferences(differences));
        }
        LOGGER.info("Current version is equal to approved version");
    }

    /**
     * Receives a list of Strings that should be verified within an Approval Test.
     * <p>
     * Within the verification process, a {@code toApprove} toApprove file is created in the build directory and
     * compared to a {@code baseline} file (if exists). In the case the versions are equal, the test passes. If no
     * baseline exists, a {@code VersionNotApprovedError} is thrown. If there is a baseline that is not equal to the
     * current version, a {@code VerificationFailedError} is thrown.
     *
     * @param data The list of Strings that should be verified
     */
    public void verify(List<String> data) {
        LOGGER.info("Starting new approval test with baseline: " + baselineName);
        if (this.data == null) {
            LOGGER.info("No approved version found");
            LOGGER.info("Creating new unapproved text file");
            fileService.createUnapprovedFile(data, baselineName);
            throw new VersionNotApprovedError(baselineName);
        }
        if (!this.data.equals(data)) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new unapproved text file");
            List<String> differences = getDifferences(this.data, data);
            fileService.createUnapprovedFile(data, baselineName);
            throw new VerificationFailedError(formatDifferences(differences));
        }
        LOGGER.info("Current version is equal to approved version");
    }

    private List<String> getDifferences(List<String> original, List<String> revised) {
        try {
            Patch<String> patch = DiffUtils.diff(original, revised);
            return UnifiedDiffUtils.generateUnifiedDiff("Baseline", "toApprove", original, patch, 0);
        } catch (DiffException e) {
            throw new RuntimeException("Cannot compute differences! " + e);
        }
    }
}
