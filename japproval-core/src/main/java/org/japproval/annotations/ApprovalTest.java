package org.japproval.annotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.japproval.parameterResolver.JsonVerifierParameterResolver;
import org.japproval.parameterResolver.StringVerifierParameterResolver;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code @ApprovalTest} is an extension of the {@code @Test} annotation and indicates the annotated test method is used
 * for approval testing.
 * <p>
 * An {@code @ApprovalTest} methods have to declare {@code @StringVerifier} or {@code @JsonVerifier} as parameter,
 * which can be used to verify Strings or JSON Objects.
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
