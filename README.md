# JUnit Approval Testing

[![Build Status](https://travis-ci.com/kklaeger/junit-approval-testing.svg?branch=master)](https://travis-ci.com/kklaeger/junit-approval-testing)
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
    testCompile group: 'org.junitapprovaltesting', name: 'junit-approval-testing', version: '1.1'
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
        classpath("org.junitapprovaltesting:approval-testing-plugin:1.1")
    }
}

apply plugin: 'differ-plugin'
apply plugin: 'approver-plugin'

```

3. Add an Approval Test:

```
@ApprovalTest(baseline="sorting1")
void testSortNames(Approver approver) {
	// arrange
	List<String> names = Arrays.asList("Peter", "Mike", "John");

	// act
	StringSorter sorter = new StringSorter();
	names = sorter.sort(names);

	// approve
	approver.verify(names);
}
```

4. Run the test to check if there are changes. These changes are printed in the error message.

5. Approve all failing tests with the following command.

`gradle approve`

During a batch process it is possible to see all changes and approve each file.

6. To highlight the changes of a specific file, use for example:

`gradle diff --file=sorting1`

7. To approve the changes of a specific file, use for example:

`gradle approve --file=sorting1`


## Components

This project consists of a core library, a gradle plugin and an example that demonstrates the functionality.

### JUnit Approval Testing

The core part of the framework that provides the Approval Testing functionality. 

##### Publish to MavenLocal

`gradle clean build install`


### JUnit Approval Testing Plugin

A gradle plugin that automates the "diff" and "approve" steps.

##### Publish to MavenLocal

`gradle clean build publishToMavenLocal`


### JUnit Approval Testing Example

Uses the testing framework and the plugin to demonstrate the functionality on a simple example.
