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
            String baselineName = getBaselineName(extensionContext);
            approver = new Approver(baselineName);
        }
        return approver;
    }

    private String getBaselineName(ExtensionContext extensionContext) {
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
        int hashCode = extensionContext.getTestMethod().get().hashCode();
        return Integer.toString(hashCode);
    }

}
