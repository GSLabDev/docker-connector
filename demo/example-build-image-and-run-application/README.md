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
	
	

## How it works:
   - From the operation window select the Build Image from Docker File operation. Enter parameters required to this operation.
   - Drag the logger onto the canvas and log #[payload] to log low level information of built image.
   - Drag the Docker connector onto the canvas, and select Run Container operation.
   - Drag the Docker connector onto the canvas, and select Inspect Container operation. Enter container name same as specified in Run Container.
   - Then drag the Object to JSON Transformer onto the canvas to return JSON response.
   - Drag the logger onto the canvas and log #[payload] to log low level information of Inspect Container.
   - Add new flow and drag Docker connector at the beginning of flow(in source). Select getContainerLogs operation and enter name of container same as specified in Run Container.
   - After you create the flows, right-click the project name in the and click Run As > Mule Application.
  