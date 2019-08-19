package org.junitapprovaltesting.approver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junitapprovaltesting.differ.Differ;
import org.junitapprovaltesting.engine.ApprovalTestingEngine;
import org.junitapprovaltesting.exceptions.*;
import org.junitapprovaltesting.model.BaselineCandidate;
import org.junitapprovaltesting.repositories.BaselineRepositoryImpl;

import java.util.List;
import java.util.Scanner;

public class Approver {

    private static Logger LOGGER = LogManager.getLogger(Approver.class);
    private BaselineRepositoryImpl baselineRepository;
    private Differ differ;

    public Approver(ApprovalTestingEngine approvalTestingEngine) {
        this.baselineRepository = (BaselineRepositoryImpl) approvalTestingEngine.getBaselineRepository();
        this.differ = approvalTestingEngine.getDiffer();
    }

    /**
     * Approves a baseline candidate by its name.
     *
     * @param name the name of the baseline candidate
     */
    public void approveBaselineCandidate(String name) {
        try {
            baselineRepository.copyBaselineCandidateToBaseline(name);
        } catch (BaselineCandidateNotFoundException e) {
            throw new ApprovingFailedException("Baseline candidate not found " + name);
        } catch (BaselineCreationFailedException e) {
            throw new ApprovingFailedException("Cannot create baseline " + name);
        } catch (CopyingFailedException e) {
            throw new ApprovingFailedException("Error while copying baseline candidate to baseline");
        }
        if (baselineRepository.removeBaselineCandidate(name)) {
            LOGGER.info("Successfully approved file " + name);
        }
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
            approveBaselineCandidate(baselineCandidate.getName());
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
        for (BaselineCandidate baselineCandidate : baselineCandidates) {
            System.out.println("Baseline candidate: " + baselineCandidate.getName());
            if (!baselineRepository.baselineExists(baselineCandidate)) {
                System.out.println("No baseline exists");
                System.out.println("Approve current version? (y/n)");
                if (userAcceptsRequest(scanner)) {
                    approveBaselineCandidate(baselineCandidate.getName());
                }
                continue;
            }
            try {
                System.out.println("Differences:\n" + baselineRepository.getDifferences(baselineCandidate));
            } catch (BaselineCandidateNotFoundException e) {
                throw new ApprovingFailedException("Baseline candidate not found " + baselineCandidate.getName());
            } catch (BaselineNotFoundException e) {
                throw new ApprovingFailedException("Baseline not found " + baselineCandidate.getName());
            }
            System.out.println("Show entire diff? (y/n)");
            if (userAcceptsRequest(scanner)) {
                differ.callExternalDiffTool(baselineCandidate.getName());
            }
            System.out.println("Approve current version? (y/n)");
            if (userAcceptsRequest(scanner)) {
                approveBaselineCandidate(baselineCandidate.getName());
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
