package org.junitapprovaltesting.sorter;


import org.junitapprovaltesting.Approver;
import org.junitapprovaltesting.annotations.ApprovalTest;

import java.util.Arrays;
import java.util.List;

public class StringSorterTest {

    @ApprovalTest(baseline = "baseline1")
    void testSortNames(Approver approver) {
        // arrange
        List<String> names = Arrays.asList("Peter", "Mike", "John");

        // act
        StringSorter sorter = new StringSorter();
        names = sorter.sort(names);

        // approve
        approver.verify(names);
    }
}
