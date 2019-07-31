package org.junitapprovaltesting.sorter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junitapprovaltesting.annotations.ApprovalTest;
import org.junitapprovaltesting.verifier.JsonVerifier;
import org.junitapprovaltesting.verifier.StringVerifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StringSorterTest {

    @ApprovalTest(baseline = "string1")
    void testString(StringVerifier stringVerifier) {
        // arrange
        String s = "Hello World";

        // act
        s = s.toLowerCase();

        // approve
        stringVerifier.verify(s);
    }

    @ApprovalTest(baseline = "sortingStrings1")
    void testSortNames(StringVerifier stringVerifier) {
        // arrange
        List<String> names = Arrays.asList("Peter", "Mike", "John");

        // act
        StringSorter sorter = new StringSorter();
        //names = sorter.sort(names);

        // approve
        stringVerifier.verify(names);
    }

    @ApprovalTest(baseline = "sortingJSON1")
    void testJSON(JsonVerifier jsonVerifier) {
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
        jsonVerifier.verify(jsonNode);
    }
}
