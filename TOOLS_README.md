## IBM Cloud starter
IBM Cloud starter for Java - MicroProfile / Java EE

[![](https://img.shields.io/badge/IBM%20Cloud-powered-blue.svg)](https://cloud.ibm.com)
[![Platform](https://img.shields.io/badge/platform-java-lightgrey.svg?style=flat)](https://www.ibm.com/developerworks/learn/java/)

### Table of Contents
* [Summary](#summary)
* [Requirements](#requirements)
* [Configuration](#configuration)
* [Project contents](#project-contents)
* [Run](#run)

### Summary

The IBM Cloud starter for Java - MicroProfile / Java EE provides a starting point for creating applications running on [WebSphere Liberty](https://developer.ibm.com/wasdev/). It contains no default com.flyingspheres.services.application code.

Once you have created an com.flyingspheres.services.application it can be deployed to IBM Cloud using a toolchain click the **Create Toolchain** button.
[![Create Toolchain](https://cloud.ibm.com/devops/graphics/create_toolchain_button.png)](https://cloud.ibm.com/devops/setup/deploy/)

### Requirements
* [Maven](https://maven.apache.org/install.html)
* Java 8: Any compliant JVM should work.
  * [Java 8 JDK from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * [Java 8 JDK from IBM (AIX, Linux, z/OS, IBM i)](http://www.ibm.com/developerworks/java/jdk/),
    or [Download a Liberty server package](https://developer.ibm.com/assets/wasdev/#filter/assetTypeFilters=PRODUCT)
    that contains the IBM JDK (Windows, Linux)

### Project contents
The project already contains IBM Cloud specific files that are used to deploy the com.flyingspheres.services.application as part of a IBM Cloud DevOps flow. The `.bluemix` directory contains files used to define the IBM Cloud toolchain and pipeline for your com.flyingspheres.services.application. The `manifest.yml` file specifies the name of your com.flyingspheres.services.application in IBM Cloud, the timeout value during deployment and which services to bind to.

### Run

To build and run an com.flyingspheres.services.application:
1. `mvn install`
1. `mvn liberty:run-server`


To run an com.flyingspheres.services.application in Docker use the Docker file called `Dockerfile`. If you do not want to install Maven locally you can use `Dockerfile-tools` to build a container with Maven installed.

### Notices

This project was generated using:
* generator-ibm-java v5.15.1
* generator-ibm-service-enablement v3.4.9
* generator-ibm-cloud-enablement v1.8.1
* generator-ibm-java-liberty v

