package org.junitapprovaltesting.differ;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.engine.ApprovalTestingEngine;
import org.junitapprovaltesting.repositories.BaselineRepositoryImpl;

public class DifferTask extends DefaultTask {

    @Input
    private String baseline;

    @Option(option = "baseline", description = "Provides the name of the baseline that should be diffed")
    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    @TaskAction
    public void differ() {
        if (baseline == null) {
            throw new RuntimeException("A specific baseline is required! Use \"gradle diff --baseline=... \"");
        }
        ApprovalTestingConfiguration approvalTestingConfiguration = new ApprovalTestingConfiguration();
        BaselineRepositoryImpl baselineRepository = new BaselineRepositoryImpl(approvalTestingConfiguration);
        ApprovalTestingEngine approvalTestingEngine =
                new ApprovalTestingEngine(baselineRepository, approvalTestingConfiguration);
        Differ differ = approvalTestingEngine.getDiffer();
        differ.callExternalDiffTool(baseline);
    }

    public String getBaseline() {
        return baseline;
    }

}