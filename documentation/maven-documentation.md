### Getting Started with Maven

This tutorial explains the necessary steps to use jApprove in a Maven project.

#### Setup

To use the entire functionality of jApprove, it is necessary to add the jApprove dependency and the Maven-Plugin to your project's pom.xml.
  
```xml
<dependencies>
    <dependency>
        <groupId>org.japprove</groupId>
        <artifactId>japprove-core</artifactId>
        <version>1.2.0</version>
    </dependency>
</dependencies>
```

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.japprove</groupId>
            <artifactId>japprove-maven-plugin</artifactId>
            <version>1.2.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>approve</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
   
To simplify the Maven commands of the plugin, it is also recommended to add the plugin group to your settings.xml (usually located in the _${user.home}/.m2/_ directory).

```xml
<settings>
    <pluginGroups>
        <pluginGroup>org.japprove</pluginGroup>
    </pluginGroups>
</settings>
```


#### Create a Simple Test

Although the main focus of jApprove is on JSON objects and API tests, this simple example should demonstrate how jApprove basically works. Therefore, a simple method that formats a list of strings is used. For each string that occurs in the list leading and trailing spaces are removed, all letters are converted to lower case and the first letter is converted to upper case.

```java
public class StringFormatter {

    public static List<String> format(List<String> strings) {
        List<String> formattedStrings = new ArrayList<>();
        for (String s : strings) {
            s = s.trim();
            s = s.toLowerCase();
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            formattedStrings.add(s);
        }
        return formattedStrings;
    }
}
```

Now, this method is tested by the usage of jApprove. An approval test is simply created by annotating the method under test with @ApprovalTest and pass the StringVerifier as a parameter. The actual test looks very similar to a usual JUnit test using the Arrange, Act and Assert (AAA) Pattern. The crucial difference is the final step where the verifier is called instead of asserting. The verifier reads the content of the string and compares it to a previously approved version. This version is stored in the so-called _baseline_ that can be set as a parameter of the @ApprovalTest annotation. If no parameter is set, a random name is used. Currently, this tool uses a text-based approach and therefore the baseline is just a simple text file.

```java
public class StringFormatterTest {

    @ApprovalTest(baseline = "strings")
    void testFormat(StringVerifier stringVerifier) {
        // arrange
        List<String> names = Arrays.asList("PETER", "MiKe", "joHn", " pAuL ");

        // act
        List<String> formattedNames = StringFormatter.format(names);

        // approve
        stringVerifier.verify(formattedNames);
    }
}
```

The test can be started like a usual JUnit test (e.g. by clicking a button within an IDE). Because there is no approved version, the test obviously fails the first time. As a side effect, the file strings.txt_ has been created in the _build/baselineCandidates/_ directory. This directory is used to store all unapproved versions. To approve this version, use the following command: 

`mvn japprove:approve -Dbaseline=strings`

With the `mvn japprove:approve` command the approve function of the Maven-Plugin is called. The -D option is used to specify the baseline of the respective test case. In this case `-Dbaseline=strings`. 

Finally, if the test is started again, it passes successfully. Meanwhile, the file strings.txt_ has been moved from _build/baselineCandidates/_ directory to a new _baselines/_ directory where all approved versions are stored.

If you make some changes to the the code (e.g. refactoring, remodularization, ...), you can simply rerun the test to verify that the behaviour of the method is still the same. If there are changes, the test fails and the the error message shows the differences. At this point you are also able to approve the new version, if the changes are accepted or even desired.

If there are many changes or a large output, it is possible to inspect the differences with the aid of an external diff tool. This tool has to be configured in the japprove.properties file that is located in the _src/test/resources/_ directory. For example, the properties file could look like the following one:
	
```
diffTool=C:/Program Files/KDiff3/kdiff3.exe diff
```

To call this tool, use the following command: 

`mvn japprove:diff -Dbaseline=strings`


#### Create a Test for a JSON Object

The test of JSON objects is very similar to the test of strings. But nevertheless, there are a few differences. To demonstrate them, a simple method that returns a simple mock state is used. The state is returned as JsonNode and consists of the actual state and a simple timestamp.

```java
public class SimpleStatus {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode getStatus() throws JsonProcessingException {
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("statusText", "OK");
        json.put("timestamp", Instant.now());
        return mapper.readTree(json.toString());
    }
}
```

To create an Approval Test for this state, it is necessary to pass a JsonVerifier instead of a StringVerifier. The JsonVerifier is designed for JsonObjects and consists of a separate implementation. That means, for example, the order of the nodes does not matter and the test passes even if you change the order. Furthermore, it enables to ignore elements of the JSON object. For example a timestamp that is different every time the test is started can simply be excluded from the test by the usage of the ignore method.

```java
public class SimpleStatusTest {

    @ApprovalTest(baseline = "status")
    public void testGetStatus(JsonVerifier jsonVerifier) throws JsonProcessingException {
        JsonNode state = SimpleStatus.getStatus();
        jsonVerifier.ignore("timestamp").verify(state);
    }
}
```

The usage of the JsonVerifier also enables testing REST APIs in the case they return JSON objects. 


#### Customize jApprove properties (optional):

By default the the approved files are stored in a _baselines/_ directory and the unapproved files in a _build/baselineCandidates/_ directory. If other locations are desired, they can be changed in the japprove.properties file in the _src/test/resources/_ directory. For example, the properties file could look like the following one:
	
```
baselineDirectory=approvedFiles
baselineCandidateDirectory=build\\unapprovedFiles
```


#### Basic Commands   

* Approve a failing test with the following command: 
 
	`mvn japprove:approve -Dbaseline=${testname}`
	
* Approve the changes of all failing test at once with the following command:

   `mvn japprove:approve -Dall`

* Start a batch process and approve/diff all failing tests step by step with the following command:

	`mvn japprove:approve`   
	
* Highlight the differences within an external diff tool with the following command: 
 
	`mvn japprove:diff -Dbaseline=${testname}`	

	
  