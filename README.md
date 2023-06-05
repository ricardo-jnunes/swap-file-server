# swap-file-server

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
