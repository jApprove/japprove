package org.junitapprovaltesting.examples;

import org.junitapprovaltesting.Approver;
import org.junitapprovaltesting.annotations.ApprovalTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple test case for the Approval Test.
 */
public class ApproverTestExamples {


    @ApprovalTest
    void testSortNames(Approver approver) {
        // arrange
        List<String> names = Arrays.asList("Peter", "Mike", "John");

        // act
        Collections.sort(names);

        // approve
        approver.verify(names);
    }

    @ApprovalTest
    void testNamesToLowerCase(Approver approver) {
        // arrange
        List<String> names = Arrays.asList("Peter", "Mike", "John");

        // act
        names.replaceAll(name -> name.toLowerCase());

        // approve
        approver.verify(names);
    }
}
