package org.junitapprovaltesting.approver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.differ.Differ;
import org.junitapprovaltesting.exceptions.*;
import org.junitapprovaltesting.model.BaselineCandidate;
import org.junitapprovaltesting.repositories.BaselineRepositoryImpl;

import java.util.List;
import java.util.Scanner;

public class Approver {

    private static Logger LOGGER = LogManager.getLogger(Approver.class);
    private BaselineRepositoryImpl baselineRepository;
    private ApprovalTestingConfiguration config;

    public Approver() {
        config = new ApprovalTestingConfiguration();
        baselineRepository = new BaselineRepositoryImpl(config);
    }

    /**
     * Approves a baseline candidate by its name.
     *
     * @param baselineCandidateName the name of the baseline candidate
     */
    public void approveBaselineCandidate(String baselineCandidateName) {
        try {
            baselineRepository.copyBaselineCandidateToBaseline(baselineCandidateName);
        } catch (BaselineCandidateNotFoundException e) {
            throw new ApprovingFailedException("Baseline candidate not found " + baselineCandidateName);
        } catch (BaselineCreationFailedException e) {
            throw new ApprovingFailedException("Cannot create baseline " + baselineCandidateName);
        } catch (CopyingFailedException e) {
            throw new ApprovingFailedException("Error while copying baseline candidate to baseline");
        }
        if (baselineRepository.removeBaselineCandidate(baselineCandidateName)) {
            LOGGER.info("Successfully approved file " + baselineCandidateName);
        }
    }

    /**
     * Approves a baseline candidate.
     *
     * @param baselineCandidate the baseline candidate
     */
    public void approveBaselineCandidate(BaselineCandidate baselineCandidate) {
        approveBaselineCandidate(baselineCandidate.getName());
    }

    /**
     * Approves all existing baseline candidates.
     */
    public void approveAllBaselineCandidates() {
        List<BaselineCandidate> baselineCandidates = baselineRepository.getBaselineCandidates();
        if (baselineCandidates.size() == 0) {
            LOGGER.info("Found no baseline candidates");
            return;
        }
        LOGGER.info("Found " + baselineCandidates.size() + " baseline candidates");
        for (BaselineCandidate baselineCandidate : baselineCandidates) {
            approveBaselineCandidate(baselineCandidate);
        }
    }

    /**
     * Starts a batch process to approve or diff all baseline candidates step by step.
     */
    public void startApprovingBatchProcess() {
        List<BaselineCandidate> baselineCandidates = baselineRepository.getBaselineCandidates();
        if (baselineCandidates.size() == 0) {
            LOGGER.info("Found no baseline candidates");
            return;
        }
        LOGGER.info("Found " + baselineCandidates.size() + " baseline candidates");
        LOGGER.info("Starting batch process ..");
        Scanner scanner = new Scanner(System.in);
        Differ differ = new Differ();
        for (BaselineCandidate baselineCandidate : baselineCandidates) {
            LOGGER.info("Baseline candidate: " + baselineCandidate.getName());
            if (!baselineRepository.baselineExists(baselineCandidate)) {
                LOGGER.info("No baseline exists");
                LOGGER.info("Approve current version? (y/n)");
                if (userAcceptsRequest(scanner)) {
                    approveBaselineCandidate(baselineCandidate);
                }
                continue;
            }
            try {
                LOGGER.info("Differences:\n" + baselineRepository.getDifferences(baselineCandidate));
            } catch (BaselineCandidateNotFoundException e) {
                throw new ApprovingFailedException("Baseline candidate not found " + baselineCandidate.getName());
            } catch (BaselineNotFoundException e) {
                throw new ApprovingFailedException("Baseline not found " + baselineCandidate.getName());
            }
            LOGGER.info("Show entire diff? (y/n)");
            if (userAcceptsRequest(scanner)) {
                differ.diff(baselineCandidate.getName());
            }
            LOGGER.info("Approve current version? (y/n)");
            if (userAcceptsRequest(scanner)) {
                approveBaselineCandidate(baselineCandidate);
            }
        }
        scanner.close();
    }

    private boolean userAcceptsRequest(Scanner scanner) {
        String input = scanner.next();
        while (!input.equals("y") && !input.equals("n")) {
            LOGGER.info("Wrong input!");
            input = scanner.next();
        }
        if (input.equals("y")) {
            return true;
        }
        return false;
    }
}
