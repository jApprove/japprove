package org.approvaltesting;

import org.junit.jupiter.api.Test;
import org.japproval.annotations.ApprovalTest;
import org.japproval.verifier.StringVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringTest {

    @ApprovalTest(baseline = "strings")
    void formatString(StringVerifier verifier) {
        List<String> names = Arrays.asList("BoB", "ALICE", "paul", "J O h N");

        List<String> formattedStrings = Formatter.formatStrings(names);

        verifier.verify(formattedStrings);
    }


}
