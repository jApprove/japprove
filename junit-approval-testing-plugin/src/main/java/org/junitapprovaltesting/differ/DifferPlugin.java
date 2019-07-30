package org.junitapprovaltesting.differ;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DifferPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getExtensions().create("differPlugin", DifferPluginExtension.class);
    }
}