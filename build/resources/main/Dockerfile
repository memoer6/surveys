FROM java:8
MAINTAINER Eduardo Rebolledo "memoer6@gmail.com"
VOLUME /var/log
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN echo America/Toronto >/etc/timezone && dpkg-reconfigure -f noninteractive tzdata
ENTRYPOINT ["java","-Dspring.profiles.active=production", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
