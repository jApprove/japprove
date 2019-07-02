# JUnit Approval Testing

An extension of JUnit5 that enables approval testing for Java-based software.

## Usage

1. Use the Approval Testing Dependency:

```
dependencies {
    testCompile group: 'org.junitapprovaltesting', name: 'junit-approval-testing', version: '1.0-SNAPSHOT'
}
```

2. Use the Approval Testing Gradle Plugin:

```
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath "org.junitapprovaltesting:ApprovalTestingPlugin:1.0"
    }
}

apply plugin: 'differ-plugin'
apply plugin: 'approver-plugin'

task showDifferences(type: org.junitapprovaltesting.differ.DifferTask) {
    doFirst {
        differPlugin.fileName = file
    }
}

task approve(type: org.junitapprovaltesting.approver.ApproverTask) {
    doFirst {
        approverPlugin.fileName = file
    }
}
```

3. Add an Approval Test:

```
@ApprovalTest
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

5. To highlight all the changes use for example:

`gradle -Pfile=org_junitapprovaltesting_sorter_StringSorterTest_testSortNames_org_junitapprovaltesting_Approver showDifferences`

6. To approve these changes use for example:

`gradle -Pfile=org_junitapprovaltesting_sorter_StringSorterTest_testSortNames_org_junitapprovaltesting_Approver approve`

## Components

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
