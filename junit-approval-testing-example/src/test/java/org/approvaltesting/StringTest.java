package org.approvaltesting;

import org.junit.jupiter.api.Test;
import org.junitapprovaltesting.annotations.ApprovalTest;
import org.junitapprovaltesting.verifier.StringVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringTest {

    @ApprovalTest(baseline = "formatNames")
    void formatNames(StringVerifier stringVerifier) {
        List<String> names = Arrays.asList("john", "P E T E R", "Bob", "Alice");

        List<String> formattedNames = Formatter.formatStrings(names);

        stringVerifier.verify(formattedNames);
    }

}
