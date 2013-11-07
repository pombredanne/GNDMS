---
title: GNDMS Installation
root: ../..
version: 0.7.0
layout: wikistyle
---


GNDMS {{ page.version }} Installation Guide
========================

**!!! Must be updated for the latest version (this is still based on 0.6.4) !!!**

This is the Installation Guide for the
[Generation N Data Management System]({{ page.root }}index.html).


* Maruku will replace this with a fine Table of Contents
{:toc}

Prerequisites
-------------

In order to build or install GNDMS, the following prerequisites need to
be fulfilled.

### Prepare your local software installation


#### Install the Java 2 SE Development Toolkit Version 1.6 
 
Please install the [Java](http://www.oracle.com/technetwork/java/index.html) SE Development
Toolkit Version 1.6.
 
For compiling the services, please make sure that `$JAVA_HOME` points
to this version and that this is also the version that is in your
`$PATH`. Naturally, this should be the same version as the one you
want to use for building and running globus.



#### Install Local UNIX Software

In order to install GNDMS, please make sure you have installed the
following software:

* openssl
* curl 
* Globus Toolkit (with GridFTP component)

Additionally, it is expected that your UNIX provides the following
shell tools: hostname, which, bash

GNDMS requires a Servlet 3.0 compliant application container. We have
tested GNDMS against the latest stable release of Jetty 6 to 8. You
can either grab Jetty from 

    http://download.eclipse.org/jetty/

or use the one which comes with your Linux distribution.


### Preparation of GNDMS Software

**ATTENTION** *The following steps need to be executed as the user that runs
the servlet container. We will assume that user to be named `gndms`.*


#### Download and Unpack GNDMS

Download either an official GNDMS distribution package and unpack it
or get the current development version from github.

Please set `$GNDMS_SOURCE` to the root directory of your GNDMS
distribution (i.e. the directory that contains `Buildfile`) and
add `$GNDMS_SOURCE/bin` to your `$PATH`.

Additionally, please set the following environment variables as specified below:

* `$GNDMS_SHARED` to a path where the databases can be stored (e.g. `/var/lib/gndms`)
* `$GNDMS_MONI_CONFIG` to `$GNDMS_SHARED/monitor.properties`

Please ensure, that the path `$GNDMS_SHARED` exists and that the user `gndms` has
permissions to read and write that directory.

* If you like the application container to run under another port than `8080` you can
  change that by setting `$GNDMS_UNSECURE_PORT`.

Also, you can set the variables
* `$GNDMS_HOST` to override the hostname,
* `$GNDMS_SECURE_PORT` to set the https port of the container,
* `$GNDMS_HOSTCERT` to set the path to the host certificate,
* `$GNDMS_HOSTKEY` to set the path to the hostkey and
* `$GNDMS_HOSTCA` to set the path to the CA.

The last three variables override settings in the `monitor.properties` file which is used to setup a running GNDMS from the console.
Nevertheless, if you missed setting one of theese options, you can change the results in the `monitor.properties` and `grid.properties` files.

Please consult `$GNDMS_SOURCE/etc/example.profile` for an
example of a properly configured environment.


#### Optionally Install Apache buildr 1.8 Locally

This step is optional.

GNDMS is built and installed using
[Apache buildr](http:///buildr.apache.org). A pre-packaged version of
buildr is included with GNDMS and can be executed by running
`$GNDMS_SOURCE/bin/gndms-buildr`.  However, if you prefer to install
buildr locally, please

* Install Ruby 1.8
* Install buildr by executing `gem install buildr`

This guide assumes the usage of the pre-packaged version of buildr.

*Currently buildr in ruby 1.9 is not supported*

Installation and Deployment from Distribution Package 
-----------------------------------------------------
 
 This section describes the actual installation of the GNDMS software
 into the Servlet Container. It requires that your system has been
 prepared as described in the previous section. Again, the following
 steps should be executed by the `gndms` user.



### Installation and Initial Deployment
 
* Please enter `$GNDMS_SOURCE` and execute `gndms-buildr package`.

  This will compile the sources and build a .war file for the application container.

     **Please consult `$GNDMS_SOURCE/doc/licensing` for details on licensing conditions of
       3rd party software components used by the GNDMS package.**

* To install the package automatically, you need to set the `$JETTY_HOME` variable to
  the jetty directory. Then execute `gndms-buildr install` in `$GNDMS_SOURCE`.

  This will install
    
  * the compiled .war file to `$JETTY_HOME/webapps/gndms.war`,
  * a grid.properties to `$JETTY_HOME/gndms` and
  * the context gndms.xml to `$JETTY_HOME/contexts`.

  If you want to use another application server than Jetty, that's
  fine: every Servlet 2.5 compliant server should do the trick.
  However, you need to deploy the application manually.
  
* Setup the service config.
  To do that, open 

      $JETTY_HOME/gndms/grid.properties 

  in an editor of your choice and check if the `baseURL` and
  `localBaseURL` entries are correct. Additionally, check if `gridPath` points to
  the desired folder. See [Server-side MyProxy setup](#serverside_myproxy_setup) for information about the myproxy entries.

* Hence GNDMS >= 0.6.2 are using SSL resp. Https for transport layer
  security, you need to activate the SSL channel of your application
  container, and provide it with decent credentials. (See [Keystore /
  Truststore Section](#keystore__truststore) )

* Before (re-)starting the application container (e.g. Jetty), make sure that the `$GNDMS_SHARED`
  path is accessible by the user gndms running the application container.

  After restarting the application container, the following lines (or
  the like) should appear in the server log (see `$GNDMS_SHARED/log/server.log`)


      =========================================================================================                                          
      GNDMS RELEASE: Generation N Data Management System VERSION: 0.6.4 "CESARE" release-0.6.4                                          
      GNDMS BUILD: built-at: Mon Sep 25 18:15:45 +0100 2012 built-by: bzcbachm@csr-pc45                                                    
      =========================================================================================                                          
      Initializing for grid: 'C3Grid' (shared dir: '/var/lib/gndms')     



At this point the GNDMS software has been successfully installed.
Next, we will describe how it may be configured for actual use.
 

 
Grid-Configuration of GNDMS Software
------------------------------------
 
### Configuring your Grid ### 

The REST-based version of GNDMS uses a plug-in system to implement
certain data-management tasks. The basic installation doesn't include
any plug-ins.
Depending on the designated role of your GNDMS installation you need to
install and setup additional plug-ins.

Additionally, there are build targets for the C3INAD project in
the `Buildfile`.


**C3-Grid Setup & Configuration for Dataprovider** 

* Enable provider-stage-in:
  Enter the directory
  
      $GNDMS_SOURCE/taskflows/staging
  
  and run
  
      gndms-buildr staging:server:package
  
  This will compile the stage-in client and server, than install it with
  
  
      gndms-buildr staging:server:deploy
  
  Now (re-)start or reload your Jetty container, this is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the provider staging plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-dataprovider.sh` and
    execute `gndms-buildr c3grid-dp-setupdb` in $GNDMS_SOURCE !

* Quick test for provider stage-in:

  We provide a script which executes a simple staging. It can be found
  under:
  
      $GNDMS_SOURCE/taskflows/staging/bin/run-staging-client.sh

  This script requires the following arguments to run:

  + The staging request as properties-file:

        -props < staging-property-file-name >

    A template for the order file can be found under

        $GNDMS_SOURCE/etc/sfr/dummy-sfr.properties

  Additionally arguments for security and the endpoints are
  required, see the section [Running a GORFX-test-client](#running_a_gorfxtestclient) for
  details.

**C3-Grid Setup & Configuration for ESGF-Stager** 

The ESGF-Staging PlugIn provides a method to stage data from ESGF to GNDMS.

* Enable ESGF-stage-in:
  Enter the directory
  
      $GNDMS_SOURCE/taskflows/esgfStaging
  
  and run
  
      gndms-buildr esgf:server:package
  
  This will compile the stage-in client and server. Then install it with
  
  
      gndms-buildr esgf:server:deploy
  
  Now (re-)start or reload your application container, this is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the provider staging plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-esgfstaging.sh` and
    execute `gndms-buildr c3grid-esgf-setupdb` in $GNDMS_SOURCE !

* Quick test for ESGF-stage-in:

  We provide a script which executes a sample ESGF staging. It can be found
  under:
  
      $GNDMS_SOURCE/taskflows/esgfStaging/bin/run-esgf-client.sh

  Before running the script, an ESGF-certificate needs to be stored (no Proxy!) in the MyProxy server configured below.
  The script requires the following arguments to run:

  + The staging request as properties-file:

        -props < staging-property-file-name >

    The property file consists of entries describing all ESG files to download and their checksums.
    A sample file can be found at $GNDMS_SOURCE/taskflows/esgfStaging/etc/order.properties.

  See section [Running a GORFX-test-client](#running_a_gorfxtestclient) for more details about
  running GORFX-test-clients.



**C3-Grid Setup & Configuration for Portal** 

* Enable GridFTP-file-transfer

  Enter the directory
  
      $GNDMS_SOURCE/taskflows/fileTransfer
  
  and run
  
      gndms-buildr transfer:server:package
  
  This will compile the client- and serverside of the plug-in. It can be installed via
  
      gndms-buildr transfer:server:deploy
  
  Now (re-)start or reload your Jetty container. This is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the GridFTP-file-transfer plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-portal.sh` and
    execute `gndms-buildr c3grid-portal-setupdb` in $GNDMS_SOURCE !

* Quick test for the file transfer:

  We provide a script which executes a simple file transfer. It can be found
  under:

      $GNDMS_SOURCE/taskflows/fileTransfer/bin/run-transfer-client.sh

  The script requires the transfer request as properties-file:

      -props < property-file-name >

  An example for transfer properties can be found in:

      $GNDMS_SOURCE/taskflows/fileTransfer/etc/order.properties

  Additionally arguments for security and the endpoints are
  required, see the section [Running a GORFX-test-client](#running_a_gorfxtestclient) for details.


* Enable InterSliceTransfer

  Enter the directory
  
      $GNDMS_SOURCE/taskflows/interSliceTransfer
  
  and run
  
      gndms-buildr interSliceTransfer:server:package
  
  This will compile the client- and serverside of the plug-in. It can be installed via
  
      gndms-buildr interSliceTransfer:server:deploy
  
  Now (re-)start or reload your Jetty container. This is required for
  the gndms plug-in loader to properly register the plug-ins.
  
* A test for the interSliceTransfer:

  We provide a script which executes different kinds of interslice transfers. It can be found
  under:

      $GNDMS_SOURCE/taskflows/interSliceTransfer/bin/run-interslicetransfer-client.sh

  The script requires the transfer request as properties-file:

      -props < property-file-name >

  An example for transfer properties can be found in:

      $GNDMS_SOURCE/taskflows/interSliceTransfer/etc/order.properties

  Additionally arguments for security and the endpoints are
  required, see the section [Running a GORFX-test-client](#running_a_gorfxtestclient) for details.



**C3-Grid Setup & Configuration for Publisher** 

* Enable Publishing

  Enter the directory
  
      $GNDMS_SOURCE/taskflows/publishing
  
  and run
  
      gndms-buildr publishing:server:package
  
  This will compile the client- and serverside of the plug-in. It can be installed via
  
      gndms-buildr publishing:server:deploy
  
  Now (re-)start or reload your Jetty container. This is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the publishing plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-publisher.sh` and
    execute `gndms-buildr c3grid-publisher-setupdb` in $GNDMS_SOURCE !

* A quick test for the publishing:

  We provide a script which tests publishing of an existing slice. It can be found
  under:

      $GNDMS_SOURCE/taskflows/publishing/bin/run-publishing-client.sh

  The script requires the order request as properties-file:

      -props < property-file-name >

  An example for the properties file can be found in:

      $GNDMS_SOURCE/taskflows/publishing/etc/order.properties


**C3-Grid Setup & Configuration for Central DMS** 

* Enable DMSStaging (DataProvider Proxy)

  Enter the directory
  
      $GNDMS_SOURCE/taskflows/DMSStaging
  
  and run
  
      gndms-buildr dmsstaging:server:package
  
  This will compile the client- and serverside of the plug-in. It can be installed via
  
      gndms-buildr dmsstaging:server:deploy
  
  Now (re-)start or reload your Jetty container. This is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the DMSStaging plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-central-dms-site.sh` and
    execute `gndms-buildr c3grid-centraldms-setupdb` in $GNDMS_SOURCE !

* A quick test for the DMSStaging:

  We provide a script which tests the staging proxy. It can be found
  under:

      $GNDMS_SOURCE/taskflows/DMSStaging/bin/run-dmsstaging-client.sh

  The script requires the order request as properties-file:

      -props < property-file-name >

  An example for the properties file can be found in:

      $GNDMS_SOURCE/etc/sfr/dummy-sfr.properties

  Indeed, this is the same file as for data provider staging.


Additionally, please consult the documentation for the respective 
community grid platform.

**NOTE** *In case of failure during setup, please execute
  `gndms-buildr kill-db` and try again.*



### Serverside MyProxy Setup ###


Most taskflows require a user certificate. Certificates are issued
from a MyProxy-Server. The server which should be used can be
configured in the `grid.properties` file. In the section: 

    # settings for the myproxy server
    myProxyServer=csr-pc28.zib.de
    myProxyConnectionCredentialFolder=/etc/grid-security
    myProxyConnectionCredentialPrefix=gndms

* `myProxyServer` holds FQDN to the MyProxy server. csr-pc28.zib.de
  can be used for testing purpose. However you need to check-in your
  D-Grid certificate-proxy using 

        myproxy-init -s csr-pc28.zib.de

  first.

* For the connection to the MyProxy-server, host authentication is used.
  This requires a Host certificate from the D-Grid together with the
  root CA-certificates.

  + `myProxyConnectionCredentialFolder` is the folder containing
  the certificates folder with the root CA-certs in hashed form.
  Simply setting it to /etc/grid-security is fine.

  + `myProxyConnectionCredentialPrefix` in case you want to use
  containercert.pem and containerkey.pem this should be `container`.
  
  However these files must be readable for the user running the
  GNDMS-application. We recommend to make a copy of these files

      containercert.pem => gndmscert.pem 
      containerkey.pem  => gndmskey.pem 

  and leave `myProxyConnectionCredentialPrefix` alone. 
  **Note** that these files must be present in the folder named by:
  `myProxyConnectionCredentialFolder`. 
  
### Keystore / Truststore ###

For the SSL connection to work most application-servers need a Java
Keystore containing the host-certificate and a truststore containing
all trusted CA certificates. Key- and truststore can be contained in
the same store.

When migrating from previous Grid installation, these root CA
certificates can usually by found in `/etc/grid-security/certificates`.
In order to import the certificates into your keystore use the
following code shell commands:

      export gridCACertPath=/etc/grid-security/certificates
      for i in $gridCACertPath/*.0; do
         ali=$( basename $i .0 );
         echo importing $ali;
         openssl x509 -in $i | keytool -keystore <path-to-jks> -import -trustcacerts -alias $ali -storepass <masterpwd> -noprompt
      done

where `<masterpwd>` is the new master password for the keystore.

**NOTE** If `<path-to-jks>` points to a not existing keystore a new one will be created. 

Then the host credentials (here in `gndmskey.pem` and `gndmscert.pem`) are loaded into a pkcs12 store.

      openssl pkcs12 -inkey /etc/grid-security/gndmskey.pem -in /etc/grid-security/gndmscert.pem -export -out jetty.pkcs12
      
where a new user password `<userpwd>` is asked for. This key is then added to the keystore using

      keytool -importkeystore -srckeystore jetty.pkcs12 -srcstoretype PKCS12 -destkeystore <path-to-jks>

where first the master password for the keystore `<masterpwd>` and then the user password `<userpwd>` have to be entered.

In `grid.properties`, the following variables have to be defined:

      trustStoreLocation=<path-to-jks>
      trustStorePassword=<masterpwd>
      keyStoreLocation=<path-to-jks>
      keyStorePassword=<masterpwd>
      keyPassword=<userpwd>

**Setting up an SSLContext in Jetty**

For Jetty, the SSL configuration is defined in `$JETTY_HOME/etc/jetty-ssl.xml`

It must be configured as follows

     <New id="sslContextFactory" class="org.eclipse.jetty.http.ssl.SslContextFactory">
         <Set name="KeyStore"><Property name="jetty.home" default="." />/etc/keystore</Set>
         <Set name="KeyStorePassword">OBF:1uo71zly1y101ym51z0h1zsp1w261u9l1sov1u9x1w1c1zt11z0d1ym91y0q1zlk1unr</Set>
         <Set name="KeyManagerPassword">OBF:1v921x1b1v9k</Set>
         <Set name="TrustStore"><Property name="jetty.home" default="." />/etc/keystore</Set>
         <Set name="TrustStorePassword">OBF:1uo71zly1y101ym51z0h1zsp1w261u9l1sov1u9x1w1c1zt11z0d1ym91y0q1zlk1unr</Set>
         <Set name="WantClientAuth">true</Set> <!-- importend for cert based authentication -->
         <Set name="CertAlias">1</Set>
         <Set name="ValidatePeerCerts">true</Set>
         <Set name="CrlPath">/tmp/1149214e.r0</Set>
    </New>

**NOTE** *KeyStorePassword and TrustStorePassword are the obfuscated version of `<masterpwd>` and 
      KeyManagerPassword is the obfuscated form of `<userpwd>`.*

To generate an obfuscate passwords execute the following command in $JETTY_HOME

    java -cp lib/jetty-http-8.0.4.v20111024.jar:lib/jetty-util-8.0.4.v20111024.jar org.eclipse.jetty.http.security.Password  <passwd>

The *KeyStore* and *TrustStore* entrys should contain `<path-to-jks>` ($JETTY_HOME/etc/keystore in this example).


Then start jetty with:

    java -jar start.jar etc/jetty-ssl.xml

**Setting up an SSLContext in Tomcat**

Please consult the fine documentation provided [here](http://static.springsource.org/spring-security/site/docs/3.0.x/reference/x509.html#x509-ssl-config).


### Finalize Installation 

Nothing todo here ;-)

**Congratulations** *At this point the installation is complete and you
have a running installation of GNDMS.*


### Running a GORFX-test-client

All tests for GORFX taskflow test-clients are derived from the same class and thus
support the same options which are:

    -cancel N             : ms to wait before destroying taskClient.
    -con-props VAL        : contract.properties
    -dn VAL               : DN
    -keystore VAL         : Keystore to use.
    -keystorePasswd VAL   : Password for keystore.
    -myProxyLogin VAL     : login name for the MyProxyServer.
    -myProxyPasswd VAL    : password name for the MyProxyServer.
    -privkeyPasswd VAL    : Password for private key in keystore.
    -props VAL            : taskflow order properties
    -truststore VAL       : truststore to use.
    -truststorePasswd VAL : Password for truststore.
    -uri URI              : URL of GORFX-Server


A few explanations of the required arguments:

+ The URI is expected to be the FQDN of the server which should
  execute your order.

+ The properties given with 

    -props  < properties-file >

  must point to a properties file for the given taskflow.


+ If SSL shall be used, a keystore and possibly a truststore must be
  provided. 

    - The keystore is defined with its password and the password for the private key stored in the keystore:

        -keystore < keystore >
        -keystorePasswd < keystore password >
        -privkeyPasswd < private key password >

      The keystore can either be a Java keystore (JKS), as default, or a store in PKCS12
      format, in which case `<keystore>` must end with either `p12` or `pkcs12`.

    - The truststore holds the required root-CA certs and should contain at
      least the DFN-Grid CA certificate.

        -truststore < truststore-location >
        -truststorePasswd < truststore-passwd >
 
      The truststore itself is expected to be a JKS. If these options are
      omitted then it is assumed that the keystore holds the required root-CA
      certificates. If the truststore is passwordless than the password
      argument can be omitted.

+ The DN of the user of which behalf the request is carried out.

    -dn < distinguish name >

    Note that you have to use the dn of your certificate if it's not a host certificate.

+ Login details for the MyProxyServer the same you used with myproxy-init. 

    -myProxyLogin  < login >          
    -myProxyPasswd < pass-phrase >       


**A complete commandline**

The following example is for the inter-slice-transfer, apart from the
runner script name it would be fine for an arbitrary taskflow and should be executed in that taskflow's folder:

    bin/run-interslicetransfer-client.sh \
    -uri https://csr-pc15.zib.de:8443/gndms/c3grid -props etc/order.properties \
    -keystore my-client-keystore -keystorePasswd foo -privkeyPasswd bar \
    -myProxyLogin mjorra -myProxyPasswd myProxyPass \
    -dn "/C=DE/O=GridGermany/OU=Konrad-Zuse-Zentrum fuer Informationstechnik Berlin (ZIB)/CN=Maik Jorra"

The command line subjects that trustroots are contained in
*my-client-keystore*, which is protected by the passphrase *foo* and
that the userkey is *bar*. Additionally, the MyProxy credentials are
stored for the user *mjorra*, protected by the password *myProxyPass*.

### Trouble Shooting

**Server responds with BAD REQUEST 400 after submitting a TaskFlow**
: It might by that the plug-ins for the TaskFlow aren't installed
correctly. Check if the plug-in path in your
`$JETTY_HOME/gndms/grid.properties` is set correctly and that the
folder contains the required jar files.


**I'm using Ubuntu and GNDMS won't start and complains about invalid ELF format**
: Yep, thats what you get for using Ubuntu... Problem is that we use
JNA for low level file-system access like chmod and Ubuntu doesn't
provide a valid libc in the usual path, just a stupid script.
To get it working you need to tell JNA where the real libc is located,
usually you do this by supplying 

        -Djna.library.path=< path to your libc > 

: when starting the application server 

: For jetty something like:
 
        java -Djna.library.path=/lib/i386-linux-gnu -jar start.jar

: should be fine.

**The client hangs after the "`Copy gsiftp: ...`" message.**
: This can be a problem with your firewall configuration. It
happens when the control-channel can be established but the
data-channel is blocked. Please check your firewall setup especially
if the `GLOBUS_TCP_PORT_RANGE` environment variable is set correctly
and is forwarded by the firewall.

**I'm getting a "`GSSException: Defective credential detected`" exception.**
: This can have to reasons: your certificate-proxy maybe outdated or
doesn't exist or your CA directory isn't up to date. In the first case
just call `grid-proxy-init` again, in the second refer to the
`fetch-crl` section <a href="#fetch-crl">below</a>.

**I'm getting a java.lang.IllegalStateException from the first transfer**
: If the exception message contains the custom message:
<pre><code>
...
Custom message: Server refused changing directory (error code 1) [Nested exception message:  
    Custom message: Unexpected reply: 550-/some/dir/2257abd0-433a-11e0-bb7b-9a0f3bfb91a0: 
        Could not change directory. : System error in stat: Permission denied
...
</code></pre>
Please make sure that `/some/dir` is owned by the gndms user and has
permissions 1777. Also make sure that your GRAM setup (especially the
sudoers entries) is correct.


**The file transfer throws an execption:**
: If the exeption looks something like
<pre><code>
    java.lang.IllegalStateException: File transfer from 
        gsiftp://some.foo.org:2811/tmp/srcDir to 
        gsiftp://more.bar.org/tmp/gndms/RW/f521ba10-a06a-11df-b70c-f2b2b7430fda failure 
        Server refused performing the request. ...`
</code></pre>
: or the client prints out infinite `Waiting for transfer to finish...`
messages and the destination directory contains a single empty file,
please ensure that both grid-ftp servers are running, accepting your
credential and can talk to each other. The best way to verify this is to
search the test-clients output for a line like:
<pre><code>
    Copy gsiftp://some.foo.org:2811/tmp/srcDir ->
        gsiftp://more.bar.org/tmp/gndms/RW/f521ba10-a06a-11df-b70c-f2b2b7430fda
</code></pre>
: and try `globus-url-copy` with:
<pre><code>
    globus-url-copy gsiftp://some.for.org:2811/tmp/srcDir/someFile \
        gsiftp://more.bar.org/tmp/gndms/RW/f521ba10-a06a-11df-b70c-f2b2b7430fda/targetFile
</code></pre>
: If you get an error message like "No route to host" or the like
ensure that the grid-ftp servers of both hosts are listening on the
right network device and that no firewall is blocking the connection.
If this hangs infinitely something with the data-channel setup is
wrong. Consult the grid-ftp documentation about the --data-channel
argument.


Advanced Configuration
----------------------

### Resetting the Database

First, **shutdown the application container**.  Next, in `$GNDMS_SOURCE`, issue

   gndms-buildr kill-db

This will delete your database.





Building GNDMS from Source
--------------------------

See above.

#### Regeneration of Javadocs

Manually delete `$GLOBUS_LOCATION/doc/api`. Now regenerate the
javadocs by executing

    gndms-buildr apidocs




#### Packaging GNDMS

In case you want do distribute your own spin-of GNDMS, we suggest you
follow the procedure described below when making a release:

    cd $GNDMS_SOURCE
    vi Buildfile # Set VERSION_NUMBER and optionally VERSION_NAME *)
    gndms-buildr release-build
    git commit -m "Made a Release"
    git tag gndms-release-ver
    git push --tags origin master
    find $GNDMS_SOURCE -type f -name '\*.class' -exec rm '{}' \;
    find $GNDMS_SOURCE -type d -name classes | xargs rm -fR
    rm .gitignore
    # Additionally delete 
    #   \*/target 
    #   name/gndms-name
    # 
    buildr apidocs
    cd ..
    mv $GNDMS_SOURCE $GNDMS_SOURCE/../gndms-release-ver
    # mac only: export COPYFILE_DISABLE=true
    tar zcvf GNDMS-Releases/gndms-release-ver.tgz --exclude .git \
        --exclude \*.ipr --exclude \*.iml --exclude \*.iws \
        --exclude \*.DS_Store --exclude \*._.DS_Store gndms-release-ver
    mv $GNDMS_SOURCE/../gndms-release-ver $GNDMS_SOURCE  # done*


Now, please upload the tarball and let the world know about it.

\*) *Please note: Every time you change the VERSION_NUMBER you have to
call `install-deps` or building the services will not succeed.*