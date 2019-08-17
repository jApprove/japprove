package org.junitapprovaltesting.differ;

import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.exceptions.DiffingFailedException;
import org.junitapprovaltesting.repositories.BaselineRepositoryImpl;

import java.io.File;
import java.io.IOException;

public class Differ {

    private ApprovalTestingConfiguration config;
    private BaselineRepositoryImpl baselineRepository;

    public Differ() {
        config = new ApprovalTestingConfiguration();
        baselineRepository = new BaselineRepositoryImpl(config);
    }

    /**
     * Computes the differences of the baseline candidate and the baseline.
     *
     * @param baselineCandidateName the name of the baseline candidate for which the differences should be computed
     */
    public void diff(String baselineCandidateName) {
        File baselineCandidate;
        try {
            baselineCandidate = baselineRepository.getBaselineCandidateAsFile(baselineCandidateName);
        } catch (IOException e) {
            throw new DiffingFailedException("Baseline candidate " + baselineCandidateName + " not found!");
        }
        File baseline;
        try {
            baseline = baselineRepository.getBaselineAsFile(baselineCandidateName);
        } catch (IOException e) {
            throw new DiffingFailedException("Baseline " + baselineCandidateName + " not found!");
        }
        String diffTool = config.getDiffTool();
        String cmd = diffTool + " " + baselineCandidate.getPath() + " " + baseline.getPath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new DiffingFailedException("Diff tool " + diffTool + " not found!");
        }
    }
}
