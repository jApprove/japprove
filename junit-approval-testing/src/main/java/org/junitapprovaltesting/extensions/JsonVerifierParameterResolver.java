package org.junitapprovaltesting.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junitapprovaltesting.verifier.JsonVerifier;

/**
 * {@code JsonVerifierParameterResolver} allows to use the {@link JsonVerifier} as parameter in Approval Tests.
 *
 * @see ParameterResolver
 */
public class JsonVerifierParameterResolver extends ApprovalTestParameterResolver implements ParameterResolver {

    /**
     * Returns true if the {@link JsonVerifier} is supported as a parameter.
     *
     * @see ParameterResolver
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == JsonVerifier.class;
    }

    /**
     * Resolve an argument for the {@link JsonVerifier}.
     *
     * @see ParameterResolver
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Object jsonVerifier = null;
        if (supportsParameter(parameterContext, extensionContext)) {
            String baselineName = getBaselineName(extensionContext);
            jsonVerifier = new JsonVerifier(baselineName);
        }
        return jsonVerifier;
    }
}
