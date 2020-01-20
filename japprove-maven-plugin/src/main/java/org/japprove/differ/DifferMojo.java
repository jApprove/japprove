package org.japprove.differ;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.japprove.config.ApprovalTestingConfiguration;
import org.japprove.engine.ApprovalTestingEngine;
import org.japprove.repositories.BaselineRepositoryImpl;

@Mojo(name = "diff")
public class DifferMojo extends AbstractMojo {

    @Parameter(property = "baseline")
    private String baseline;

    /**
     * The task that contains the options to show the differences of baseline candidates to the
     * respective baseline.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (baseline == null) {
            throw new RuntimeException(
                    "A specific baseline is required! Use \"mvn japprove:diff -Dbaseline=... \"");
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
}
