package org.junitapprovaltesting.differ;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.tools.Differ;

public class DifferTask extends DefaultTask {

    @Input
    private String fileName;

    @Option(option = "file", description = "Provides the name of the file that should be diffed")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @TaskAction
    public void differ() {
        if (fileName == null) {
            throw new RuntimeException("A specific file is required! Use \"gradle diff --file=... \"");
        }
        ApprovalTestingConfiguration config = new ApprovalTestingConfiguration();
        Differ differ = new Differ(config);
        differ.diff(fileName);
    }

}