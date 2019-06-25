package org.junitapprovaltesting.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junitapprovaltesting.Approver;

/**
 * The ApprovalTestParameterResolver is used to inject the {@link Approver} into the tests annotated with @ApprovalTest.
 */
public class ApprovalTestParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Approver.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Object approver = null;
        if (this.supportsParameter(parameterContext, extensionContext)) {
            approver = new Approver(formatName(extensionContext.getTestMethod().get().toString()));
        }
        return approver;
    }

    private String formatName(String name) {
        return name.replace("void ", "_").replace(".", "_").replace("(", "_").replace(")", "");
    }
}
