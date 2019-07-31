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
            jsonNode = objectMapper.readTree("{\n" +
                    "    \"store\": {\n" +
                    "        \"book\": [\n" +
                    "            {\n" +
                    "                \"category\": \"reference\",\n" +
                    "                \"author\": \"Nigel Rees\",\n" +
                    "                \"title\": \"Sayings of the Century\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"category\": \"fiction\",\n" +
                    "                \"author\": \"Evelyn Waugh\",\n" +
                    "                \"title\": \"Sword of Honour\",\n" +
                    "                \"price\": 15456.99\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"category\": \"fiction\",\n" +
                    "                \"author\": \"Herman Melville\",\n" +
                    "                \"title\": \"Moby Dick 2\",\n" +
                    "                \"isbn\": \"0-553-21311-3\",\n" +
                    "                \"price\": 84564.99\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"category\": \"fiction\",\n" +
                    "                \"author\": \"J. R. R. Tolkien\",\n" +
                    "                \"title\": \"The Lord of the Rings\",\n" +
                    "                \"isbn\": \"0-395-19395-8\",\n" +
                    "                \"price\": 22.5499\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"bicycle\": {\n" +
                    "            \"color\": \"red\",\n" +
                    "            \"price\": 19.95\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"expensive\": 10\n" +
                    "}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // act

        // approve
        jsonVerifier.ignore("$.store.book[*].price").verify(jsonNode);
    }
}
