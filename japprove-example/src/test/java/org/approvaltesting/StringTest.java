package org.approvaltesting;

import org.japprove.annotations.ApprovalTest;
import org.japprove.verifier.StringVerifier;

import java.util.Arrays;
import java.util.List;

public class StringTest {

    @ApprovalTest(baseline = "strings")
    void formatString(StringVerifier verifier) {
        List<String> names = Arrays.asList("BoB", "ALICE", "paul", "J O h N");

        List<String> formattedStrings = Formatter.formatStrings(names);

        verifier.verify(formattedStrings);
    }
}
