package org.junitapprovaltesting.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class ApprovalTestParameterResolver {

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
