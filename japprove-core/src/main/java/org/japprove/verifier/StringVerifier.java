package org.japprove.verifier;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.japprove.engine.ApprovalTestingEngine;
import org.japprove.exceptions.BaselineCandidateCreationFailedException;
import org.japprove.exceptions.BaselineNotFoundException;
import org.japprove.exceptions.VerificationFailedException;
import org.japprove.exceptions.errors.VerificationFailedError;
import org.japprove.exceptions.errors.VersionNotApprovedError;

/**
 * The {@code StringVerifier} provides methods to verify String objects or String lists within
 * Approval Tests.
 */
public class StringVerifier extends Verifier {

    private static final Logger LOGGER = LogManager.getLogger(StringVerifier.class);

    public StringVerifier(ApprovalTestingEngine approvalTestingEngine) {
        super(approvalTestingEngine);
    }

    /**
     * Receives a String that should be verified within an Approval Test.
     *
     * <p>Within the verification process, the passed data is compared to the data in the
     * corresponding baseline. In the case the versions are equal, the test passes. If no baseline
     * exists, a {@code VersionNotApprovedError} is thrown. If there is a baseline that is not equal
     * to the current version, a {@code VerificationFailedError} is thrown.
     *
     * @param data The String that should be verified
     */
    public void verify(String data) {
        LOGGER.info("Starting new approval test with baseline: " + baselineName);
        String baselineData;
        try {
            baselineData = baselineRepository.getContentOfTextBaseline(baselineName);
        } catch (BaselineNotFoundException e) {
            LOGGER.info("No approved version found");
            LOGGER.info("Creating new baseline candidate");
            createBaselineCandidate(data);
            throw new VersionNotApprovedError(baselineName);
        }
        if (!baselineData.equals(data)) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new baseline candidate");
            String differences = differ.getDifferences(baselineData, data);
            createBaselineCandidate(data);
            throw new VerificationFailedError(differences);
        }
        LOGGER.info("Current version is equal to approved version");
        baselineRepository.removeBaselineCandidate(baselineName);
    }

    /**
     * Receives a List of Strings that should be verified within an Approval Test.
     *
     * <p>Within the verification process, the passed data is compared to the data in the
     * corresponding baseline. In the case the versions are equal, the test passes. If no baseline
     * exists, a {@code VersionNotApprovedError} is thrown. If there is a baseline that is not equal
     * to the current version, a {@code VerificationFailedError} is thrown.
     *
     * @param data The String that should be verified
     */
    public void verify(List<String> data) {
        verify(String.join("\n", data));
    }

    private void createBaselineCandidate(String data) {
        try {
            baselineRepository.createBaselineCandidate(data, baselineName);
        } catch (BaselineCandidateCreationFailedException e) {
            throw new VerificationFailedException("Internal error while creating baseline");
        }
    }
}
