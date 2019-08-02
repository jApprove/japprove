package org.junitapprovaltesting.approver;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ApproverPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getTasks().create("approve", ApproverTask.class);
    }
}
