# swap-file-server

This Spring Boot application aims to allow file exchange on a server through an API call.

## Use Case
A configuration file is being used by a tool, and for some reason, that tool requires a different configuration. Through the API call, you can swap out this file online.

I know there are other methods to accomplish this type of task, but if needed... we have this.

## RUN

java -jar swap-file-server-0.0.1-SNAPSHOT.jar


## Settings of the files that will be swapping between them

Configurations in src\main\resources\application.properties


# How to use

Simple validation of parameter Request-Credential (needs improvement for request security)

```
curl --location --request POST '192.168.113.2:8048/rest/swapfile' \
--header 'Request-Credential: someMd5HashOrRequestSecurityImplementation'
```
