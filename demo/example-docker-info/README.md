# README.md

## Get the docker details from docker host

### Introduction
    This is demo about getting system-wide information from docker.
  
### How to demo:
- Set the following properties in ``` src/main/resources/mule-app-DEV.properties ``` :
	1. docker.dockerHost - This is the Host name or IP of your docker engine host.
    2. docker.port - This is the port exposed by docker engine.
    3. docker.APIVersion - This is the API version of docker engine.

- Deploy the example in a Mule Runtime and hit localhost:8081/info

### How it works:
	- The dockerInfo connect to docker host on specified port and displays system wide information regarding the Docker installation.
	- Information displayed includes number of Containers Running, Containers Paused, Containers Stopped, Images information, Registry Configurations, the kernel version, and memory details.