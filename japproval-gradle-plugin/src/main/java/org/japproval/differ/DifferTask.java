package org.japproval.differ;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.japproval.config.ApprovalTestingConfiguration;
import org.japproval.engine.ApprovalTestingEngine;
import org.japproval.repositories.BaselineRepositoryImpl;

/**
 * The task that contains the options to show the differences of baseline candidates to the
 * respective baseline.
 */
public class DifferTask extends DefaultTask {

    @Input
    private String baseline;

    @Option(option = "baseline",
            description = "Provides the name of the baseline that should be diffed")
    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    /**
     * Calls the external diff tool for a specific baseline.
     */
    @TaskAction
    public void differ() {
        if (baseline == null) {
            throw new RuntimeException(
                    "A specific baseline is required! Use \"gradle diff --baseline=... \"");
        }
        ApprovalTestingConfiguration approvalTestingConfiguration =
                new ApprovalTestingConfiguration();
        BaselineRepositoryImpl baselineRepository =
                new BaselineRepositoryImpl(approvalTestingConfiguration);
        ApprovalTestingEngine approvalTestingEngine =
                new ApprovalTestingEngine(baselineRepository, approvalTestingConfiguration);
        Differ differ = approvalTestingEngine.getDiffer();
        differ.callExternalDiffTool(baseline);
    }

    public String getBaseline() {
        return baseline;
    }
}