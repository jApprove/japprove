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
            jsonNode = objectMapper.readTree("[\n" +
                    "\t{\n" +
                    "\t\t\"id\": \"0001\",\n" +
                    "\t\t\"type\": \"donut\",\n" +
                    "\t\t\"name\": \"Cake\",\n" +
                    "\t\t\"ppu\": 0.55,\n" +
                    "\t\t\"batters\":\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"batter\":\n" +
                    "\t\t\t\t\t[\n" +
                    "\t\t\t\t\t\t{ \"id\": \"1001\", \"type\": \"Regular\" },\n" +
                    "\t\t\t\t\t\t{ \"id\": \"1001\", \"type\": \"Regular\" },\n" +
                    "\t\t\t\t\t\t{ \"id\": \"1002\", \"type\": \"Chocolate\" }\n" +
                    "\t\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\"topping\":\n" +
                    "\t\t\t[\n" +
                    "\t\t\t\t{ \"id\": \"5001\", \"type\": \"None\" },\n" +
                    "\t\t\t\t{ \"id\": \"5002\", \"type\": \"Glazed\" },\n" +
                    "\t\t\t\t{ \"id\": \"5005\", \"type\": \"Sugar\" },\n" +
                    "\t\t\t\t{ \"id\": \"5007\", \"type\": \"Powdered Sugar\" },\n" +
                    "\t\t\t\t{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },\n" +
                    "\t\t\t\t{ \"id\": \"5003\", \"type\": \"Chocolate\" },\n" +
                    "\t\t\t\t{ \"id\": \"5004\", \"type\": \"Maple\" },\n" +
                    "\t\t\t\t{ \"id\": \"1004\", \"type\": \"Devil's Food\" }\n" +
                    "\t\t\t]\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"id\": \"0002\",\n" +
                    "\t\t\"type\": \"donut\",\n" +
                    "\t\t\"name\": \"Raised\",\n" +
                    "\t\t\"ppu\": 0.55,\n" +
                    "\t\t\"batters\":\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"batter\":\n" +
                    "\t\t\t\t\t[\n" +
                    "\t\t\t\t\t\t{ \"id\": \"1001\", \"type\": \"Regular\" }\n" +
                    "\t\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\"topping\":\n" +
                    "\t\t\t[\n" +
                    "\t\t\t\t{ \"type\": \"None\", \"id\": \"5001\" },\n" +
                    "\t\t\t\t{ \"id\": \"5002\", \"type\": \"Glazed\" },\n" +
                    "\t\t\t\t{ \"id\": \"5005\", \"type\": \"Sugar\" },\n" +
                    "\t\t\t\t{ \"id\": \"5003\", \"type\": \"Chocolate\" },\n" +
                    "\t\t\t\t{ \"id\": \"5004\", \"type\": \"Maple\" }\n" +
                    "\t\t\t]\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"id\": \"0003\",\n" +
                    "\t\t\"type\": \"donut\",\n" +
                    "\t\t\"name\": \"Old Fashioned\",\n" +
                    "\t\t\"ppu\": 0.55,\n" +
                    "\t\t\"batters\":\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"batter\":\n" +
                    "\t\t\t\t\t[\n" +
                    "\t\t\t\t\t\t{ \"id\": \"1001\", \"type\": \"Regular\" },\n" +
                    "\t\t\t\t\t\t{ \"id\": \"1002\", \"type\": \"Chocolate\" },\n" +
                    "\t\t\t\t\t\t{ \"id\": \"1002\", \"type\": \"Strawberry\" }\n" +
                    "\t\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\"topping\":\n" +
                    "\t\t\t[\n" +
                    "\t\t\t\t{ \"id\": \"5001\", \"type\": \"None and Maple\" },\n" +
                    "\t\t\t\t{ \"id\": \"5002\", \"type\": \"Glazed\" },\n" +
                    "\t\t\t\t{ \"id\": \"5003\", \"type\": \"Chocolate\" },\n" +
                    "\t\t\t\t{ \"id\": \"5004\", \"type\": \"Maple\" }\n" +
                    "\t\t\t]\n" +
                    "\t}\n" +
                    "]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        approver.verify(jsonNode);
    }
}
