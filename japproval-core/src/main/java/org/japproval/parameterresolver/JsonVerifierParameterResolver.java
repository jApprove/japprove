package org.japproval.parameterresolver;

import org.japproval.config.ApprovalTestingConfiguration;
import org.japproval.engine.ApprovalTestingEngine;
import org.japproval.repositories.BaselineRepositoryImpl;
import org.japproval.verifier.JsonVerifier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * {@code JsonVerifierParameterResolver} allows to use the {@link JsonVerifier} as parameter in
 * Approval Tests.
 *
 * @see ParameterResolver
 */
public class JsonVerifierParameterResolver extends ApprovalTestParameterResolver
        implements ParameterResolver {

    /**
     * Returns true if the {@link JsonVerifier} is supported as a parameter.
     *
     * @see ParameterResolver
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == JsonVerifier.class;
    }

    /**
     * Resolve an argument for the {@link JsonVerifier}.
     *
     * @see ParameterResolver
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Object jsonVerifier = null;
        if (supportsParameter(parameterContext, extensionContext)) {
            String baselineName = getBaselineName(extensionContext);
            ApprovalTestingConfiguration approvalTestingConfiguration =
                    new ApprovalTestingConfiguration();
            BaselineRepositoryImpl baselineRepository =
                    new BaselineRepositoryImpl(approvalTestingConfiguration);
            ApprovalTestingEngine approvalTestingEngine =
                    new ApprovalTestingEngine(baselineRepository, approvalTestingConfiguration,
                            baselineName);
            jsonVerifier = approvalTestingEngine.getJsonVerifier();
        }
        return jsonVerifier;
    }
}
