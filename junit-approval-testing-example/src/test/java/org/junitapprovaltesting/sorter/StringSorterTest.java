package org.junitapprovaltesting.sorter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junitapprovaltesting.Approver;
import org.junitapprovaltesting.annotations.ApprovalTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StringSorterTest {

    @ApprovalTest(baseline = "string1")
    void testString(Approver approver) {
        // arrange
        String s = "Hello World";

        // act
        s.toLowerCase();

        // approve
        approver.verify(s);
    }

    @ApprovalTest(baseline = "sortingStrings1")
    void testSortNames(Approver approver) {
        // arrange
        List<String> names = Arrays.asList("Peter", "Mike", "John");

        // act
        StringSorter sorter = new StringSorter();
        names = sorter.sort(names);

        // approve
        approver.verify(names);
    }

    @ApprovalTest(baseline = "sortingJSON1")
    void testJSON(Approver approver) {
        // arrange
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree("{ \"name\":\"Johni\", \"age\":30, \"time\":1564558998 }");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // act

        // approve
        approver.verify(jsonNode);
    }
}
