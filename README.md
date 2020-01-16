# JApprove

[![Build Status](https://travis-ci.com/jApprove/japprove.svg?branch=master)](https://travis-ci.com/jApprove/japprove)
[![GitHub version](https://badge.fury.io/gh/jApprove%2Fjapprove.svg)](http://badge.fury.io/gh/jApprove%2Fjapprove)
![License](https://img.shields.io/badge/License-Apache%20License%202.0-brightgreen.svg)

An extension of JUnit5 that enables approval testing for Java-based software.

## Features

Currently, this Approval Testing framework contains the following features:

- Approval Testing of Strings and JSON Objects
- Approval Testing of REST APIs
- Highlighting differences
- Approving changes


## Usage

1. Use the jApprove Dependency that is located on the Maven Central Repository in the following way:

   ```
   repositories {
       mavenCentral()
   }
   dependencies {
       testImplementation("org.japprove:japprove-core:1.2.0")
   }
   ```

2. Use the jApprove Gradle Plugin in the following way:

   ```
   buildscript {
       repositories {
           mavenCentral()
       }
       dependencies {
           classpath("org.japprove:japprove-gradle-plugin:1.2.0")
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

4. Run the test to check whether there are changes comparing to a previously approved version. In the case there are changes, they are printed in the error message. 

    Note: If there is no approved version, the test fails anyway. In this case you first have to approve the current version. 

5. Approve the failing tests with the following command:  

   `gradle approve`  
   
   During a starting batch process it is possible to see all changes and approve each failing test.
   
   To approve the changes of all failing test at once, use the following command:

   `gradle approve --all`

   To approve the changes of a specific failing test, use for example:

   `gradle approve --baseline=sorting1`

6. To get a better overview of all changes, it is possible to highlight the changes of a failing test within an external diff tool. Use for example:

   `gradle diff --baseline=sorting1`

7. Customize jApprove properties (optional):

	Create a "japprove.properties" file in the "src/main/resources" directory. In this file it is possible to specify the following values:
	
	- The directory of the baseline
	- The temporary directory of the unapproved files
	- A custom diff tool
	
	For example, the properties file could look like the following one:
	
	```
	baselineDirectory=baselines\\
	baselineCandidateDirectory=build\\baselineCandidates\\
	diffTool=C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.2.3\\bin\\idea64 diff

	```
	
	The properties of the file above are the default properties.
	
  
## Components

This project consists of a core library, a gradle plugin and an example that demonstrates the functionality. 

### jApprove Core

The core part of this tool is an extension of the jUnit testing framework. It enables to use the @ApprovalTest annotation to mark a test as an approval test and run them like usual unit tests.


### jApprove Gradle Plugin

A gradle plugin that automates the "diff" and "approve" steps.

### jApprove Example

Uses the testing framework and the plugin to demonstrate the functionality on a simple example.
