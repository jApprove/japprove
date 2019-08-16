package org.junitapprovaltesting.differ;

import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.exceptions.BaselineCandidateNotFoundException;
import org.junitapprovaltesting.exceptions.BaselineNotFoundException;
import org.junitapprovaltesting.exceptions.DiffingFailedException;
import org.junitapprovaltesting.files.TextFile;
import org.junitapprovaltesting.repositories.BaselineRepository;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Differ {

    private ApprovalTestingConfiguration config;
    private BaselineRepository baselineRepository;

    public Differ() {
        config = new ApprovalTestingConfiguration();
        baselineRepository = new BaselineRepository(config);
    }

    /**
     * Computes the differences of two {@link TextFile}s by the baselineName.
     *
     * @param baselineName the name of the baseline for which the differences should be computed
     */
    public void diff(String baselineName) {
        TextFile baselineCandidate;
        try {
            baselineCandidate = baselineRepository.getBaselineCandidate(baselineName);
        } catch (FileNotFoundException e) {
            throw new BaselineCandidateNotFoundException(baselineName);
        }
        TextFile baseline;
        try {
            baseline = baselineRepository.getBaseline(baselineCandidate.getName());
        } catch (FileNotFoundException e) {
            throw new BaselineNotFoundException(baselineName);
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
