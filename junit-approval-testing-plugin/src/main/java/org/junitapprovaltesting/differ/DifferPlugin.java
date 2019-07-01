package org.junitapprovaltesting.differ;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.junitapprovaltesting.PluginExtension;

public class DifferPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getExtensions().create("differPlugin", PluginExtension.class);
    }
}