# HTTP Server

## Table of contents

- [Overview](#overview)
  - [Technologies used](#technologies-used)
- [Challenges faced](#challenges-faced)
- [Learning outcomes](#learning-outcomes)
- [Directory structure](#directory-structure)
- [Collaboration](#collaboration)
- [Contact me](#contact-me)

## Overview
This repositary has the code for a HTTP server written in Java. This does not require any dependencies written in Java using the ServerSocket and other classes in java.net package.

### Technologies used
- Java
- ServerSocket
- GZip

## Challenges faced
### 1. Working with ServerSocket
- This project is the first time I have used the ServerSocket class in Java. I started with the basics such as how to create a local server and setting up basic settings such as allowing port reuse to prevent errors when rerunning the server. Also how to accept a incoming connection using `accept()` method.

```java
ServerSocket serverSocket = new ServerSocket(4221);
serverSocket.setReuseAddress(true);
Socket clientSocket = serverSocket.accept();
```

### 2. Working with Streams
- Working with ServerSocket also requires us to work with the input and output streams attached to it. We can access them using this piece of code:
```java
InputStream input = clientSocket.getInputStream();
OutputStream output = clientSocket.getOutputStream();
BufferedReader reader = new BufferedReader(new InputStreamReader(input));
```
- BufferedReader gives us access to the request that is received by the server. Using this, we perform backend operations and return required information to the client. 
- For example, the first line that is inside BufferedReader is the request URL. This contains the request method, request target and HTTP version. Using these data, we can properly delegate the task to methods which perform the requested task.

### 3. GZip compression in Java
- Again for the first time I used the GZip compression library in Java. In Java there is a inbuilt class named `GZIPOutputStream` from package `java.util.zip`. This contains also other methods for zip compression.
- For my usecase of compressing echoed message using GZip I created this helper function:
```java
public byte[] compressMessage(byte[] msg) throws IOException {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream(msg.length);
    GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);
    zipStream.write(msg);

    zipStream.finish();

    return byteStream.toByteArray();
}
```
- This method takes the message bytes as input, compresses them using GZip and returns the compressed bytes which are written to the output stream of our client.

## Learning outcomes
- HTTP Protocol
- TCP
- File Handling in Java
- GZip compression in Java

## Directory structure

```
.
├── README.md
├── codecrafters.yml
├── pom.xml
├── src
│   └── main
│       └── java
│           ├── ConnectionHandler.java
│           ├── FileHandler.java
│           ├── Main.java
│           └── RequestHandler.java
├── target
│   ├── archive-tmp
│   ├── classes
│   │   ├── ConnectionHandler.class
│   │   ├── FileHandler.class
│   │   ├── Main.class
│   │   └── RequestHandler.class
│   ├── codecrafters-http-server-1.0.jar
│   ├── generated-sources
│   │   └── annotations
│   ├── maven-archiver
│   │   └── pom.properties
│   ├── maven-status
│   │   └── maven-compiler-plugin
│   │       └── compile
│   │           └── default-compile
│   │               ├── createdFiles.lst
│   │               └── inputFiles.lst
│   └── test-classes
└── your_program.sh

15 directories, 16 files
```

## Collaboration

If you have found a bug, suggesting an improvement or want to collaborate then please raise an [issue](https://github.com/DarshanVaishya/http-server/issues) or create an [pull request](https://github.com/DarshanVaishya/http-server/pulls).

## Contact me

- [LinkedIn](https://www.linkedin.com/in/darshan-vaishya-ba99001a9/)
