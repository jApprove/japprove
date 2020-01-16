package org.japprove.parameterresolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

/**
 * The parent class of the ParameterResolvers.
 */
public abstract class ApprovalTestParameterResolver {

    private static final String EMPTY_STRING = "";
    private static final String BACKSLASH = "\"";

    public abstract boolean supportsParameter(ParameterContext parameterContext,
                                              ExtensionContext extensionContext)
            throws ParameterResolutionException;

    public abstract Object resolveParameter(ParameterContext parameterContext,
                                            ExtensionContext extensionContext)
            throws ParameterResolutionException;

    String getBaselineName(ExtensionContext extensionContext) {
        String annotationParameter = getAnnotationParameter(extensionContext);
        if (annotationParameter.equals(EMPTY_STRING)) {
            return createBaselineNameByHashCode(extensionContext);
        } else {
            return annotationParameter;
        }
    }

    private String getAnnotationParameter(ExtensionContext extensionContext) {
        String declaredAnnotation = extensionContext
                .getTestMethod()
                .get()
                .getDeclaredAnnotations()[0]
                .toString();
        declaredAnnotation = declaredAnnotation
                .substring(declaredAnnotation.indexOf(BACKSLASH) + 1);
        declaredAnnotation = declaredAnnotation.substring(0, declaredAnnotation.indexOf(BACKSLASH));
        return declaredAnnotation;
    }

    private String createBaselineNameByHashCode(ExtensionContext extensionContext) {
        long hashCodeClass = Math.abs(extensionContext.getTestClass().get().hashCode());
        long hashCodeMethod = Math.abs(extensionContext.getTestMethod().get().hashCode());
        String filename = Long.toString(hashCodeClass) + hashCodeMethod;
        return filename;
    }
}
