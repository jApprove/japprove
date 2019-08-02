package org.junitapprovaltesting.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

/**
 * The parent class of the ParameterResolvers.
 */
public abstract class ApprovalTestParameterResolver {

    public abstract boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException;

    public abstract Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException;

    String getBaselineName(ExtensionContext extensionContext) {
        String annotationParameter = getAnnotationParameter(extensionContext);
        if (annotationParameter.equals("")) {
            return createBaselineNameByHashCode(extensionContext);
        } else {
            return annotationParameter;
        }
    }

    private String getAnnotationParameter(ExtensionContext extensionContext) {
        String declaredAnnotation = extensionContext.getTestMethod().get().getDeclaredAnnotations()[0].toString();
        declaredAnnotation = declaredAnnotation.substring(declaredAnnotation.indexOf("\"") + 1);
        declaredAnnotation = declaredAnnotation.substring(0, declaredAnnotation.indexOf("\""));
        return declaredAnnotation;
    }

    private String createBaselineNameByHashCode(ExtensionContext extensionContext) {
        long hashCode = Math.abs(extensionContext.getTestMethod().get().hashCode());
        return Long.toString(hashCode);
    }

}
