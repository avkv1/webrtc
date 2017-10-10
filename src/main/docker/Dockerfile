FROM anapsix/docker-oracle-java8
VOLUME /tmp
ADD target/*.jar app.jar
ENTRYPOINT ["java","-server","-Djava.security.egd=file:/dev/./urandom","-Xmx512M","-Xms512M","-XX:+CMSClassUnloadingEnabled","-XX:+UseConcMarkSweepGC","-jar","/app.jar"]