# WebRTC
WebRTC signalling server + simple client implementation

## How to launch

### The docker way

```
docker pull avkvl/webrtc
docker run -p 8080:8080 avkvl/webrtc
```

### The maven way

```
mvn spring-boot:run
```

## How to use

Then in a browser open 2 tabs to http://localhost:8080

And push to Join button

## Built With

* [Spring Boot](https://spring.io/guides/gs/spring-boot/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
