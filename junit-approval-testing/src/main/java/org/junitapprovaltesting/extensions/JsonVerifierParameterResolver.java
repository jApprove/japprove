package org.junitapprovaltesting.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junitapprovaltesting.verifier.JsonVerifier;

public class JsonVerifierParameterResolver extends ApprovalTestParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == JsonVerifier.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Object jsonVerifier = null;
        if (this.supportsParameter(parameterContext, extensionContext)) {
            String baselineName = getBaselineName(extensionContext);
            jsonVerifier = new JsonVerifier(baselineName);
        }
        return jsonVerifier;
    }
}
