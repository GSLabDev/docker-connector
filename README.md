# README.md

## Docker Anypoint™ Connector
Docker Connector is a communication tool that provides seamless integration with the Docker engine from a mule flow. It exposes Docker operations by executing API calls as per configuration. It supports *HTTP* and *HTTPS* connections and can be used as inbound, inbount streaming and outbound connector from the mule flow. Docker connector is build using docker-java client. 

## Supported Mule runtime versions
Mule 3.8.5

## Supported Docker versions
Docker API version 1.24 or above

## Installation 

Install connector in Anypoint Studio following the instructions at [Installing Connectors from Anypoint Exchange](https://docs.mulesoft.com/mule-user-guide/v/3.9/installing-connectors) 

To configure and use the Docker connector refer [user manual](doc/docker-connector-user-manual.adoc) 

## Authentication
* Docker host can be accessed in two different ways from a client (Mule runtime / Anypoint studio ): 
    - Without Authentication (HTTP)
    - With Certificate based authentication (HTTPS)

### WITHOUT AUTHENTICATION (HTTP)
Provide docker host and port in a global configuration of docker connector. Without authentication use is generally recommended for internal applications where Docker daemon is running on HTTP port.

To make the docker daemon listening on a HTTP port:
- Stop Docker daemon if running:
```
$service docker stop
```
- Start Docker daemon on http:
```
$dockerd -H=0.0.0.0:2375
```

### CERTIFICATE BASED AUTHENTICATION (HTTPS)
- Provide docker host, port and certificate paths in a global configuration of docker connector. Docker daemon is running on HTTPS port.
Certificates required in this authentication mechanism are -
    - CA certificate (ca.pem)
    - Server certificate (server-cert.pem)
    - Server key (server-key.pem)
    - Client certificate (cert.pem)
    - Client key (key.pem)
    
***Note : For more information refer [Docker Documentation](https://docs.docker.com/engine/security/https/)***
    
- To make the Docker daemon listening on a https, generate server and client certificates on docker host.
    - Stop Docker daemon if running:
    ```
    $service docker stop
    ```
    - Start docker daemon on https using following command:
    ```
    $dockerd --tlsverify --tlscacert=ca.pem --tlscert=server-cert.pem --tlskey=server-key.pem -H=0.0.0.0:2376
    ```
    
    ***Note: This will start docker daemon on port 2376.
    Copy certificates (ca.pem, key.pem and cert.pem) from Docker host to Anypoint studio for HTTPS configuration.***
        
## Testing information

### To run functional tests
-   Update properties in src/test/resources/automation-credentials.properties according to which config-type you are using.
    - Properties required for HTTP docker configuration:
        - HTTP-Docker-Config.dockerHostIP= host name or IP of docker host
        - HTTP-Docker-Config.dockerHostPort=port exposed by docker host
        - HTTP-Docker-Config.apiVersion=API version of docker engine
    
    - Properties required for HTTPS docker configuration:
        - HTTPS-Docker-Config.dockerHostIP= host name or IP of docker host
        - HTTPS-Docker-Config.dockerHostPort=port exposed by docker host
        - HTTPS-Docker-Config.apiVersion=API version of docker engine
        - HTTPS-Docker-Config.apiVersion.clientCertificateDirectoryPath=Path of directory containing certificates required for docker TLS connection (ca.pem, cert.pem, key.pem)
        
- Set VM or maven arguments to -Dactiveconfiguration=HTTPS-Docker-Config or -Dactiveconfiguration=HTTP-Docker-Config depending on which config-type you are using.
- Run Junit test suite FunctionalTestSuite.java with HTTP-Docker-Config using following command: 
        ```
        mvn clean && mvn test -Dtest="*TestSuite,*Test" -Dactiveconfiguration=HTTP-Docker-Config
        ```    


## Reporting Issues

Use GitHub for tracking issues with this connector. To report new issues use this [link] (https://github.com/GSLabDev/docker-connector/issues).