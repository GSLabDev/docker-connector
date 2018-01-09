# Build image from Dockerfile and run container

## Introduction
This demo is about building a desired docker image using Dockerfile and to run a container using the docker image. It covers "Build Image from Docker File" and "Run Container" operations of Docker Connector.
  
## How to demo:
### Prerequisite:
- Create Dockerfile in src/main/resources/docker and add required commands in docker file to build image.
- We have added this commands in Dockerfile to create docker image.
```
FROM ubuntu:latest
#copy the  application to the container:
COPY runapp.sh  /
#Define working directory:
WORKDIR /
CMD  ["/bin/sh","runapp.sh"]				
```				
- Create runapp.sh file in src/main/resources/docker and add content as below:
```
#!/bin/bash
while :; do echo "Now `date`"; sleep 1; done
```
  
- Set the following properties in ``` src/main/resources/mule-app-DEV.properties ``` :
  - dockerHost - This is the Host name or IP of your docker engine host.
  - port - This is the port exposed by docker engine.
  - APIVersion - This is the API version of docker engine.
  - dockerFile - This is the path of Dockerfile which contains commands to build docker image. ( e.g. src/main/resources/docker/Dockerfile)
  - imageName - This is the name of the new build image using Dockerfile.
  - imageTag - This is a tag of the new build image using Dockerfile.	
  - containerName - This is the name of container created to run application.
  - Deploy the example in a Mule Runtime and open URL http://localhost:8081/runapp
  
### How it works:
	- Docker connector builds image using Dockerfile and run container from that image on docker host.
