FROM openJDK=11
ADD target/accountapp.jar accountapp.jar
ENTRYPOINT["JAVA","-JAR","accountapp.jar"]
EXPOSE 8096