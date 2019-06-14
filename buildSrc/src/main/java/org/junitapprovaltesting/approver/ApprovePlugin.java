package org.junitapprovaltesting.approver;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ApprovePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().create("approve", Approver.class, (task) -> {

        });
    }
}
