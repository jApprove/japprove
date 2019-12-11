package org.approvaltesting;

import com.fasterxml.jackson.databind.JsonNode;
import org.approvaltesting.utils.HttpUtils;
import org.junitapprovaltesting.annotations.ApprovalTest;
import org.junitapprovaltesting.verifier.JsonVerifier;

public class ApiTest {

    @ApprovalTest(baseline = "tasks")
    void restApiTest(JsonVerifier jsonVerifier) {
        String url = "http://localhost:8080/tasks";
        JsonNode json = HttpUtils.callWebService(url);
        jsonVerifier.verify(json);
    }

    @ApprovalTest(baseline = "status")
    void restApiStatusTest(JsonVerifier jsonVerifier) {
        String url = "http://localhost:8080/tasks/status/John";
        JsonNode json = HttpUtils.callWebService(url);
        jsonVerifier.ignore("systemTime").verify(json);
    }
}
