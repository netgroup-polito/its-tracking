# its-tracking

The aim of this project is to design a RESTful web service able to track the presence of vehicles in an area with restricted access and, based on this information, choose if guarantee or reject the access to other vehicles. If the access is granted, the system has to provide the newly-entered vehicle with a suggested path to follow. Otherwise the vehicle is just rejected by the system with a message.

## Project Setup

### Folders organization

Project folders are organized this way:
1. server application resides in folder RNSService;
2. client application is located in folder Client;
3. in the Report folder are present all files LateX needed to modify and compile the report.
5. the root directory contains the final report with more detailed information about the project and a Postman collection useful for testing purposes.

### Prerequisites

The SO used is Ubuntu 16.04. The server is written in Java v8.  
It is deployed in Tomcat, along with Ant, and use Neo4j as DB.  
All of them are provided in the `lib` folder as .zip file. Extract them in /opt.  
Then set the following environment variables:  
`ANT_HOME=/opt/apache-ant-1.9.13`  
`CATALINA_HOME=/opt/tomcat/apache-tomcat-8.5.20`  
Use the command `export VAR=foo` in the the file /etc/environment or in ~/.bashrc.  
In the `lib` folder is also present a file named `dp2.zip`. Extract also that in /opt: it contains all the jars needed by the server.  
If any of the previous installation were different on your machine, also the relative variables in the scripts must be changed.  
A VM with the previous requirements can be found here: https://summer.ipv6.polito.it:8081/share.cgi?ssid=0fanb9R

### Server

In order to launch and compile the server application and its tests, it is available a set of ant scripts.
To setup the server it is necessary to follow these steps:
1. start Neo4j and Tomcat by calling start-neo4j-then-tomcat target of build.xml script  
From command line: `ant start-neo4j-then-tomcat -f RNSService/build.xml`  
2. deploy web service by calling redeploy target of build.xml script  
From command line: `ant redeploy -f RNSService/build.xml`.  
This step is necessary only the first time.

### Client

To setup the client (in the directory Client) it is necessary to install Node.js,
then it is possible to launch the application. To install Node.js, go to the ufficial site https://nodejs.org.

In the Client directory, the script `client_setup.sh` contains all the following commands along with those to install Node.js via `curl`.
Run it as superuser.

These are the steps to install and run only the Angular Client:
1. `cd ./Client`
2. `sudo npm install -g @angular/cli`
3. `sudo npm install`
4. `ng serve`

### Z3

Z3 is the core of the application, the optimization library used to compute the path of the vehicles.
Follow these steps to configure z3:
1. download the prebuilt version of the library from the offical GitHub repository https://github.com/Z3Prover/z3/releases (checking your SO and architecture)
2. extract it and change name of the directory to `z3`
3. put it in the /opt folder
4. redefine the environment variable LD_LIBRARY_PATH in the file /etc/environment or in ~/.bashrc with the following command:
`export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/opt/z3/bin"`  
To be sure that this step worked, one way is to check the environment variables in tomcat. This snippet of code will print them:  

    System.out.println(System.getenv("LD_LIBRARY_PATH"));  

If the correct LD_LIBRARY_PATH is not set, another way is to put the .so files contained in the z3 zip inside a folder listed in LD_LIBRARY_PATH. After that restart tomcat.
