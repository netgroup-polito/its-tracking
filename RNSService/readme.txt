This package includes some service examples programmed using JAX-RS.

Setup necessary before running the examples
-------------------------------------------
In order to run these examples you need to have Tomcat 8 installed and
configured as explained in the next section.

Tomcat 8 setup
--------------
Make sure the CATALINA_HOME environment variable has been set to the
Tomcat installation directory.

Add the following code to the Tomcat configuration file "tomcat-user.xml"

  <role rolename="manager-gui"/>
  <role rolename="manager-script"/>
  <user username="root" password="root" roles="manager-gui,manager-script"/>

In this way you have the possibility to access the Tomcat 8 manager via web
application using the root credentials.
After having started Tomcat, you can browse localhost:8080 to display the
main Tomcat page. From this page you can access the management page using the
credentials set into tomcat-user.xml.
In order for Tomcat to be able to use the JAX-RS jar files and their
dependencies, edit the Topmcat 8 catalina.properties configuration file and
make sure shared.loader is defined as follows:
shared.loader=/opt/dp2/shared/lib/*.jar

Of course, you must have installed the necessary jar files of JAX-RS and their
dependencies in /opt/dp2/shared/lib as explained in the course web site.

Note that the Tomcat 8 configuration setup has already been done on the Labinf
machines.

Eclipse Project setup
---------------------
Each web service example XXX has its own build-XXX.xml (which imports the
generic ant scripts service-build.xml and tomcat-build.xml).
Each web service example also has its own folder with a WebContent specific
for that example and an index.html (with some information about the service).
You can create a single eclipse java project for this package and then add
the jars under lib and those under /opt/dp2/shared/lib (that you should have
copied from the course web site or from Labinf machines) to the build path.
Alternatively, you can import the package as an eclipse project and then
adjust the settings of the project (e.g. the reference to the JRE).
DO NOT USE service-build.xml and tomcat-build.xml directly. Instead use the
build.xml or the build-XXX.xml of each service (which imports them).

In order to avoid duplications, the local libraries needed by the services
are located only in the main folder (under lib) and in the shared folder
/opt/dp2/shared/lib. Each build-XXX.xml will access them as necessary when
creating the .war archive.

How to run and test a service
-----------------------------
0. Start tomcat by running the start-tomcat target (from build.xml or from
any build-XXX.xml).

1. Deploy a service by running the deployWS target from the corresponding
build-XXX.xml (deployWS will first compile and package the service).

2. You can interact with a service by using any general purpose client (e.g.
the Firefox RESTClient plugin or the Chrome POSTMAN plugin or the command-line
curl tool) or the client examples (previously released in a separate package).

3. You can also use the other targets in the build-XXX.xml files for managing
the service (start/stop, undeploy, reload).

Instructions for running TelDirectory (secured service)
-------------------------------------------------------
The TelDirectory service has a peculiarity that requires extra setup:
it requires the use of https and of basic HTTP authentication (see
security settings in web.xml).
You can follow the next instructions in order to configure Tomcat
so that it can deploy the service on HTTPS (port 8443).
After this procedure you can deploy the service as with the other services.
1. Make a new (self-signed) certificate and save it in a keystore file with
alias "tomcat" and password "changeit":
   1.1 To do so, run the following command from the directory where you want the
   .keystore file to be saved:
   			keytool -genkey -alias tomcat -keyalg RSA -keystore .keystore
   	   When asked for a name, give "localhost"
       When asked for a password, give "changeit"
       You can leave the other fields unspecified

2. Configure Tomcat so that it publishes its services on HTTPS too using the
self-signed certificate:
   2.1 To do so, change tomcat configuration file conf/server.xml by
   un-commenting the following Connector:
            <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
                       maxThreads="150" scheme="https" secure="true"
                       clientAuth="false" sslProtocol="TLS"
                       keystoreFile="/home/foo/.keystore"/>
    
       Note that the last attribute has to be added, where /home/foo is the
       place where you have saved your .keystore file.

3. Check that tomcat has been configured correctly
    3.1 Browse https://localhost:8443/
        You will need to add an exception rule to your browser because the
        certificate is not trusted.
        Afterwards, you should see the Tomcat home page.

4. Check tomcat-users.xml and make sure it includes a role with the role name
written in the web.xml file (you can change it if necessary). Also check that
tomcat-users.xml includes at least a user with that role. This user should have
a username and a password.

After having deployed TelDirectory, you should be able to browse the service
index page at https://localhost:8443/teldirectory
Note that, according to the security settings in web.xml, this page can be
accessed without basic authentication. Instead, in order to use the service
resources it is necessary to authenticate with the credentials of a user
belonging to the role specified in web.xml.
A browser can be used to test TelDirectory as did with the other services.
Note also that tomcat will automatically redirect any request to the resources
of TelDirectory issued on http to the corresponding https URLs.



