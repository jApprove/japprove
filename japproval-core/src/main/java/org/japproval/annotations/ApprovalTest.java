package org.japproval.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import org.japproval.parameterresolver.JsonVerifierParameterResolver;
import org.japproval.parameterresolver.StringVerifierParameterResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * {@code @ApprovalTest} is an extension of the {@code @Test} annotation and indicates the annotated
 * test method is used for approval testing.
 *
 * <p>An {@code @ApprovalTest} methods have to declare {@code @StringVerifier} or
 * {@code @JsonVerifier} as parameter, which can be used to verify Strings or JSON Objects.
 */
@Retention(RUNTIME)
@Test
@ExtendWith(StringVerifierParameterResolver.class)
@ExtendWith(JsonVerifierParameterResolver.class)
public @interface ApprovalTest {
    /**
     * Allows to specify a name for the baseline.
     *
     * @return the name of the baseline
     */
    String baseline() default "";
}
