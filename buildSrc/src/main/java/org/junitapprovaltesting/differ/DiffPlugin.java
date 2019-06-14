package org.junitapprovaltesting.differ;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DiffPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().create("showDifferences", Differ.class, (task) -> {

        });
    }
}