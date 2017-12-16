Build image from Docker file and run container
==============================================

INTRODUCTION
    This demo is about building a desired docker image using Dockerfile and to run a container using the docker image.
	This demo covers "Build Image from Docker File" and "Run Container" operations of Docker Connector.
  
HOW TO DEMO:
  1. Prerequisite:
		A. Create Dockerfile in src/main/resources/docker and add required commands in docker file to build image.
			We have added this commands in Dockerfile to create docker image.
		
				FROM ubuntu:latest
				# copy the  application to the container:
				COPY runapp.sh  /
				# Define working directory:
				WORKDIR /
				CMD  ["/bin/sh","runapp.sh"]
	
		B. Create runapp.sh file in src/main/resources/docker and add content as below:
				#!/bin/bash
				while :; do echo "Now `date`"; sleep 1; done
  
  2. Following properties are used in the demo:
    a. dockerHost - This is the Host name or IP of your docker engine host.
    b. port - This is the port exposed by docker engine.
  	c. APIVersion - This is the API version of docker engine.
  	d. dockerFile - This is the path of Dockerfile which contains commands to build docker image. ( e.g. src/main/resources/docker/Dockerfile)
    e. imageName - This is the name of the new build image using Dockerfile.
    f. imageTag - This is a tag of the new build image using Dockerfile.	
	g. containerName - This is the name of container created to run application.
	
	

HOW IT WORKS:	
   - From the operation window select the Build Image from Docker File operation. Enter parameters required to this operation.
   - Drag the logger onto the canvas and log #[payload] to log low level information of built image.
   - Drag the Docker connector onto the canvas, and select Run Container operation.
   - Drag the Docker connector onto the canvas, and select Inspect Container operation. Enter container name same as specified in Run Container.
   - Then drag the Object to JSON Transformer onto the canvas to return JSON response.
   - Drag the logger onto the canvas and log #[payload] to log low level information of Inspect Container.
   - Add new flow and drag Docker connector at the beginning of flow(in source). Select getContainerLogs operation and enter name of container same as specified in Run Container.
   - After you create the flows, right-click the project name in the and click Run As > Mule Application.
  