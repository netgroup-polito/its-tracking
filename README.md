#  its-tracking
The aim of this project is to design a RESTful web service able to track the precence of vehicles in an area with restricted access and, based on this information, choose if guarantee or reject the access to other vehicles. If the access is granted, the system has to provide the newly-entered vehicle with a suggested path to follow. Otherwise the vehicle is just rejected by the system with a message.

# Folders organization
Project folders are organized this way:
1. server application resides in folder RNSService;
2. client application is located in folder Client;
3. in the Report folder are present all files LateX needed to compile correctly the report.

The final report contains more detailed information about the project.

# Project Setup

## Server
In order to launch and compile the server application (in the directory RNSService) and its tests, it is available a set of ant scripts. In particular:
1. ant script neo4j-build.xml provides a set of target to launch/stop/restart&clean neo4j database;
2. ant script build.xml provides all the target to start/stop tomcat, to deploy the application and to run tests, it relies upon another script to define the operation of such targets, that is build-rns.xml.

The targets can be either launched via Eclipse IDE or command line.
To setup the server up and running it is necessary to follow these step:
1. start Neo4j by using start-neo4j target of neo4j-build.xml script.
From command line `ant  'start-neo4j' -f /path/to/neo4j-build.xml`;
2. start Tomcat by calling start-tomcat target of build.xml script.
From command line: `ant  'start-tomcat' -f /path/to/build.xml` ;
3. deploy web service by calling redeploy target of build.xml script.
From command line: `ant 'redeploy  -f /path/to/build.xml`.

In order to launch the tests written for the service it is necessary to use rns-
tests target of build.xml script.

## Client
To setup the client (in the directory Client) it is necessary to install Node.js,
then it is possible to launch the application. To get Node.js, go to nodejs.org.
These are the steps:
1. go to dir Client
2. install the angular CLI running the following command:
`npm install -g @angular/cli`
3. launch the app running the following command:
`ng serve`

## Z3
The development enviroment that has been used is Ubuntu. In order to use
Z3 library in such operating system, there are a couple of steps one has to
follow.
1. download the prebuilt version of the library from the offical GitHub
repository https://github.com/Z3Prover/z3/releases;
2. once extracted the files, we have to place them in a specific location
in which we will command other applications to look for the needed
classes;
3. after everything has been placed in the chosen location, we have to
define an environment variable which will allow the Java application
to know where the Z3 library is located. For Ubuntu such variable is
LD_LIBRARY_PATH. This variable must point to the location of the
bin folder of the extracted Z3 library;
It can be  defined globally in the file /etc/environment, but the same result
could have been achieve defining it locally for the used in /Home/.bashrc
file;
4. last step, is to add the Z3 .jar file to the build path of our project.
This is an automatic configuration and Tomcat will load the dynamic libraries
in the bin folder by itself. If this is not sufficient, we need to manually put in
the WebContent/WEB-INF/lib/jni the Z3 library and point LD LIBRARY PATH
to that folder. Double check that the .jar file is present in the copied folder.
Please make sure, in order to use the library in Tomcat, to have correctly set
CATALINA_HOME variable, pointing to Tomcat folder and JAVA HOME
variable (provided you have installed Java on the machine).
