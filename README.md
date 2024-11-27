# HTTP Server

## Table of contents

- [Overview](#overview)
  - [How to run](#how-to-run)
  - [Functionality implemented](#functionality-implemented)
  - [Technologies used](#technologies-used)
- [Challenges faced](#challenges-faced)
- [Learning outcomes](#learning-outcomes)
- [Directory structure](#directory-structure)
- [Collaboration](#collaboration)
- [Contact me](#contact-me)

## Overview
This repository has the code for a HTTP server written in Java. This does not require any dependencies written in Java using the ServerSocket and other classes in java.net package.

### How to run
- The code can be run by compiling the Java files in `src/main/java`.
```
// Inside src/main/java folder
javac *.java
```
- Once compiled run the command
```
java Main --directory /tmp/
```
- You can change the directory used for file based requests by changing the arguement sent while running the class file. For example to change it to Desktop use:
```
java Main --directory /Desktop/
```

### Functionality implemented
#### 1. GET Home
```
curl -v http://localhost:4221
```
- Returns status code 200.

#### 2. GET URL Path
```
curl -v http://localhost:4221/abcdefg
```
- Returns status code 200 or 404 based on its existene.

#### 3. Echo a message
```
curl -v http://localhost:4221/echo/hello
```
- Returns status code 200 with the message in its body, with header that has the size of the echoed message.

#### 4. Read and send back header (user-agent)
```
curl -v --header "User-Agent: foobar/1.2.3" http://localhost:4221/user-agent
```
- Returns status code 200 with the given user agent in its body.

#### 5. Read a file on the server
```
curl -i http://localhost:4221/files/foo
```
- Returns with status code 200 and the contents of the specified file in its body if the file exists. Otherwise returns with status code 404 not found.

#### 6. Write a file on the server
```
curl -v --data "12345" -H "Content-Type: application/octet-stream" http://localhost:4221/files/file_123
```
- This creates a file on the server named `file_123` with the content `12345`. The server returns status code 201 Created.

#### 7. GZip compression
```
curl -v -H "Accept-Encoding: gzip" http://localhost:4221/echo/abc`
```
- This returns the echoed message `abc` but compressed using GZip.

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
└── your_program.sh
```

## Collaboration

If you have found a bug, suggesting an improvement or want to collaborate then please raise an [issue](https://github.com/DarshanVaishya/http-server/issues) or create an [pull request](https://github.com/DarshanVaishya/http-server/pulls).

## Contact me

- [LinkedIn](https://www.linkedin.com/in/darshan-vaishya-ba99001a9/)
