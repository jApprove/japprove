package org.junitapprovaltesting.annotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitapprovaltesting.extensions.JsonVerifierParameterResolver;
import org.junitapprovaltesting.extensions.StringVerifierParameterResolver;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Test
@ExtendWith(StringVerifierParameterResolver.class)
@ExtendWith(JsonVerifierParameterResolver.class)
public @interface ApprovalTest {
    String baseline() default "";
}
