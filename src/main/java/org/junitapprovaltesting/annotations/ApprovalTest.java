package org.junitapprovaltesting.annotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitapprovaltesting.extensions.ApprovalTestParameterResolver;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Test
@ExtendWith(ApprovalTestParameterResolver.class)
public @interface ApprovalTest {
}
