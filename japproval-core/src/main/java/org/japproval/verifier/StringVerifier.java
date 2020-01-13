package org.japproval.verifier;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.japproval.engine.ApprovalTestingEngine;
import org.japproval.exceptions.BaselineCandidateCreationFailedException;
import org.japproval.exceptions.BaselineNotFoundException;
import org.japproval.exceptions.VerificationFailedException;
import org.japproval.exceptions.errors.VerificationFailedError;
import org.japproval.exceptions.errors.VersionNotApprovedError;

import java.util.List;

/**
 * The {@code StringVerifier} provides methods to verify String objects or String lists within Approval Tests.
 */
public class StringVerifier extends Verifier {

    private static final Logger LOGGER = LogManager.getLogger(StringVerifier.class);

    public StringVerifier(ApprovalTestingEngine approvalTestingEngine) {
        super(approvalTestingEngine);
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

    private void createBaselineCandidate(String data) {
        try {
            baselineRepository.createBaselineCandidate(data, baselineName);
        } catch (BaselineCandidateCreationFailedException e) {
            throw new VerificationFailedException("Internal error while creating baseline");
        }
    }


}
