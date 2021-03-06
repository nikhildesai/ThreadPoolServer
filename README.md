# ThreadPoolServer
A simple web server that uses a fixed-size thread-pool to handle requests. 

# Supported requests

This web server only supports 'GET /' e.g. curl http://localhost:8080. This will return HTTP 200 OK with a success message
  
# Design

- The maven-exec-plugin is used to run the main method in the WebServerContainer class. 
- A ServerFactory is used to create a server that listens on a given port and has a given thread-pool size
- The FixedPoolWebServer itself runs on a separate thread. It will create new Worker threads for each new request until it reaches the limit. After that the threads in the pool are re-used. Incoming requests are kept in a queue if all threads are busy
- Unit tests as well integration tests can be found under src/test/java and are written using jUnit and EasyMock

# Pre-requisites

1. Maven 3.0.3+
2. Java 1.6+

# Install

git clone https://github.com/nikhildesai/ThreadPoolServer.git

# Compile

cd ThreadPoolServer/

mvn compile

# Run tests

mvn clean test

# Run server

mvn compile exec:java

# Generate eclipse project files

mvn eclipse:clean eclipse:eclipse
