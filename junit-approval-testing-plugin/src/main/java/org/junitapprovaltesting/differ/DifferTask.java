package org.junitapprovaltesting.differ;


import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.junitapprovaltesting.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        File baseline;
        try {
            baseline = FileUtils.getBaseline(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Found no approved version for passed file " + fileName);
        }
        String cmd = IDEA_DIFF + " " + FileUtils.getToApprove(fileName).getPath() + " " + baseline.getPath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Diff tool " + IDEA_DIFF + " not found!");
        }
    }

}