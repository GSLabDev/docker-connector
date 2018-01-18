# Docker Connector Demo
## Get the docker details from docker host
### Introduction
This demo is about getting system-wide information from docker host.

### Pre-requisites
1. Docker engine running on TCP
2. Minimum Mule Server 3.8.5 EE

### Preparation
1. Import this DEMO in Anypoint Studio going to ***File → Import → Anypoint Studio Project from External Location***, select the demo project root and choose as server runtime ***Mule Server 3.8.5 EE*** or above version.
2. Once imported, in ***src/main/resources/*** you will find mule-app-DEV.properties file, this contains all required properties to make the DEMO work. Open it. It will look like:

```
#Docker configuration
docker.host=
docker.port=
docker.APIVersion=
```

3. Fill each property with the required value:
	
Field Name        | Value
-------------     | -------------
docker.dockerHost | Host name or IP of your docker engine host
docker.port       | Port exposed by docker engine
docker.APIVersion | API version of docker engine

4. In ***Anypoint Studio***, Right click in the project folder → Run As → Mule Application.

5. If the Mule App is deployed correctly, hit hit <http://localhost:8081/info>.

### How it works:
- The dockerInfo connect to docker host on specified port and displays system wide information regarding the Docker installation.
- Information displayed includes number of Containers Running, Containers Paused, Containers Stopped, Images information, Registry Configurations, the kernel version, and memory details.