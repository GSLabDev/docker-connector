# Docker Connector Demo
## Build image from Dockerfile and run container

### Introduction
This demo is about building a desired docker image using Dockerfile and to run a container using the docker image. It covers different operations of docker connector like Build Image from Docker File, Push Image, Pull Image, Run Container and Inspect Container.

### Pre-requisites
1. Docker engine running on TCP
2. Docker registry running on localhost:5000
2. Minimum Mule Server 3.8.5 EE

### Preparation
1. Import this DEMO in Anypoint Studio going to ***File → Import…​ → Anypoint Studio Project from External Location***, select the demo project root and choose as server runtime ***Mule Server 3.8.5 EE*** or above version.
2. Once imported, in ***src/main/resources/*** you will find mule-app-DEV.properties file, this contains all required properties to make the DEMO work. Open it. It will look like:

```
#Docker configuration
docker.host=
docker.port=
docker.APIVersion=

# Build Image from docker file parameters
build.dockerFile=src/main/resources/docker/Dockerfile
build.imageName=localhost:5000/echoapp
build.imageTag=test

# Run container and application parameters
app.containerName=docker-echoapp-container
```

3. Fill empty property with the required value:
	
Field Name        | Value
-------------     | -------------
docker.dockerHost | Host name or IP of your docker engine host
docker.port       | Port exposed by docker engine
docker.APIVersion | API version of docker engine

***Note:*** *Change build.imageName if your docker registry is running on other than localhost:5000*

4. In ***Anypoint Studio***, Right click in the project folder → Run As → Mule Application.

5. If the Mule App is deployed correctly, hit hit <http://localhost:8081/runapp>.
  
### How it works:
- ***Build an image from Docker file***: builds image using *src/main/resources/docker/Dockerfile*. This operation use *runapp.sh* as an application file from *src/main/resources/docker/*.
- ***Push Image***: push built image in above operation on local docker registry.
- ***Pull Image***: pull image from docker registry.
- ***Run Container***: create and run container from pulled image.
- ***Inspect Container***: gets low-level information of the container created in *Run Container* operation.
