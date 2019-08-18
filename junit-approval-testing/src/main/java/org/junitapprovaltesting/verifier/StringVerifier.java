package org.junitapprovaltesting.verifier;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.engine.ApprovalTestingEngine;
import org.junitapprovaltesting.exceptions.BaselineCandidateCreationFailedException;
import org.junitapprovaltesting.exceptions.BaselineNotFoundException;
import org.junitapprovaltesting.exceptions.VerificationFailedException;
import org.junitapprovaltesting.exceptions.errors.VerificationFailedError;
import org.junitapprovaltesting.exceptions.errors.VersionNotApprovedError;

import java.util.List;

/**
 * The {@code StringVerifier} provides methods to verify String objects or String lists within Approval Tests.
 */
public class StringVerifier extends Verifier {

    private static final Logger LOGGER = LogManager.getLogger(StringVerifier.class);
    private String baselineData;

    public StringVerifier(ApprovalTestingEngine approvalTestingEngine) {
        super(approvalTestingEngine);
        try {
            baselineData = baselineRepository.getContentOfTextBaseline(baseline);
        } catch (BaselineNotFoundException e) {
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
            try {
                baselineRepository.createBaselineCandidate(data, baseline);
            } catch (BaselineCandidateCreationFailedException e) {
                throw new VerificationFailedException("Internal error while creating baseline");
            }
            throw new VersionNotApprovedError(baseline);
        }
        if (!this.baselineData.equals(data)) {
            LOGGER.info("Current version is not equal to approved version");
            LOGGER.info("Create new baseline candidate");
            String differences = differ.getDifferences(this.baselineData, data);
            try {
                baselineRepository.createBaselineCandidate(data, baseline);
            } catch (BaselineCandidateCreationFailedException e) {
                throw new VerificationFailedException("Internal error while creating baseline");
            }
            throw new VerificationFailedError(differences);
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

}
