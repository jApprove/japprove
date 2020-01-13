# JUnit Approval Testing

[![Build Status](https://travis-ci.com/kklaeger/junit-approval-testing.svg?branch=master)](https://travis-ci.com/kklaeger/junit-approval-testing)
[![GitHub version](https://badge.fury.io/gh/kklaeger%2Fjunit-approval-testing.svg)](http://badge.fury.io/gh/kklaeger%2Fjunit-approval-testing)
![License](https://img.shields.io/badge/License-Apache%20License%202.0-brightgreen.svg)

An extension of JUnit5 that enables approval testing for Java-based software.

## Features:

Currently, this Approval Testing framework contains the following features:

- Approval Testing of Strings and JSON Objects
- Approval Testing of REST APIs
- Highlighting differences
- Approving changes


## Usage

1. Use the Approval Testing Dependency:

   ```
   dependencies {
       compile("org.junitapprovaltesting:junit-approval-testing:1.2.0-SNAPSHOT")
   }
   ```

2. Use the Approval Testing Gradle Plugin:

   ```
   buildscript {
       repositories {
           mavenCentral()
           mavenLocal()
       }
       dependencies {
           classpath("org.junitapprovaltesting:approval-testing-plugin:1.2.0-SNAPSHOT")
       }
   }

   apply plugin: 'differ-plugin'
   apply plugin: 'approver-plugin'
   
   ```

3. Add an Approval Test:

   ```
   @ApprovalTest(baseline="sorting1")
   void testSortNames(StringVerifier stringVerifier) {
	   // arrange
	   List<String> names = Arrays.asList("Peter", "Mike", "John");
   
	   // act
	   StringSorter sorter = new StringSorter();
	   names = sorter.sort(names);
   
	   // approve
	   stringVerifier.verify(names);
   }
   ```

4. Run the test to check if there are changes. These changes are printed in the error message.

5. Approve the failing tests with the following command:  

   `gradle approve`  
   
   During a starting batch process it is possible to see all changes and approve each failing test. To approve the changes of all failing test at once, use the following command:

   `gradle approve --all`

   To approve the changes of a specific failing test, use for example:

   `gradle approve --baseline=sorting1`

   To highlight the changes of a specific failing test, use for example:

   `gradle diff --baseline=sorting1`


6. Customize approval testing properties (optional):

	Create a "approvaltesting.properties" file in the "src/main/resources" directory. In this file it is possible to specify the following values:
	
	- The directory of the baseline
	- The temporary directory of the unapproved files
	- A custom diff tool
	
	For example, the properties file could look like the following one:
	
	```
	baselineDirectory=baselines\\
	baselineCandidateDirectory=build\\baselineCandidates\\
	diffTool=C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.1.3\\bin\\idea64 diff

	```
	
	The properties of the file above are the default properties.
	
  
## Components

This project consists of a core library, a gradle plugin and an example that demonstrates the functionality. Currently, the library and the plugin are not published. Therefore, it is necessary to build them manually.

### JUnit Approval Testing

The core part of the framework that provides the Approval Testing functionality. 

##### Publish to MavenLocal

```
cd junit-approval-testing
gradle clean build install
``` 

### JUnit Approval Testing Plugin

A gradle plugin that automates the "diff" and "approve" steps.

##### Publish to MavenLocal
```
cd junit-approval-testing-plugin
gradle clean build publishToMavenLocal
``` 

### JUnit Approval Testing Example

Uses the testing framework and the plugin to demonstrate the functionality on a simple example.
