Get the information from docker 
==============================

INTRODUCTION
    This is demo about getting docker info from docker engine.
  
HOW TO DEMO:
  1. Set the following properties in src/main/resources/mule-app-DEV.properties:
    a. docker.dockerHost - This is the Host name or IP of your docker engine host.
    b. docker.port - This is the port exposed by docker engine.
    c. docker.APIVersion - This is the API version of docker engine.

  2. Deploy the example in a Mule Runtime and hit localhost:8081/info

HOW IT WORKS:
   -This command displays system wide information regarding the Docker installation. Information displayed includes the kernel version, number of containers and images.