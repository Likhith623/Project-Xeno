FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ChannelStubServer.java .
RUN javac ChannelStubServer.java
EXPOSE 5000
CMD ["java", "ChannelStubServer"]