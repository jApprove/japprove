package org.junitapprovaltesting.differ;


import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.tools.Differ;

public class DifferTask extends DefaultTask {

    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.1.3\\bin\\idea64 diff";

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
        Differ differ = new Differ();
        differ.diff(fileName);
    }

}