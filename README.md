# JApprove

[![Build Status](https://travis-ci.com/jApprove/japprove.svg?branch=master)](https://travis-ci.com/jApprove/japprove)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.japprove/japprove-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.japprove/japprove-core)
[![License](https://img.shields.io/badge/License-Apache%20License%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

jApprove is a testing tool that provides an easy and flexible way to use approval testing in Java-based software. The basic idea of this tool is to extend JUnit 5 to obtain familiar test cases and to use standard build tools to highlight and approve changes. The main focus of this tool is on the test of large JSON objects and REST APIs.


### Features

Currently, jApprove contains the following features:

* Approval Testing of Strings and JSON Objects
* Straightforward execution of the tests from inside an IDE
* Simple approving and diffing functionality by the usage of Gradle or Maven 
* Run Approval Tests in a CI Pipeline
* Modular structure to simply replace components or add new features


### Getting Started

The usage of jApprove depends on the build tool that is used. Therefore, there are tutorials for Gradle- and Maven-based projects.

* [Getting Started with jApprove (using Gradle)](documentation/gradle-documentation.md)
* [Getting Started with jApprove (using Maven)](documentation/maven-documentation.md)


### Examples

For both Gradle and also Maven there is an example project that uses jApproval to demonstrate the usage.

* [Gradle Example Project](https://github.com/jApprove/japprove-gradle-example)
* [Maven Example Project](https://github.com/jApprove/japprove-maven-example)





