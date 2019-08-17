package org.junitapprovaltesting.differ;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

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
        Differ differ = new Differ();
        differ.diff(baseline);
    }

}