FROM java:8

EXPOSE 8080

#将宿主机上的jar包复制到容器中 命名为app.jar
ADD vueBlog-java--0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'


LABEL authors="lenovo"

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=pro"]