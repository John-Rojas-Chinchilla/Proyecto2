# ChatApp Desktop Communication System

ChatApp Desktop Communication System is a Java desktop chat application that enables users to register, log in, exchange messages, and manage contacts in a local network environment.

## Features

- User registration and login
- Contact management
- Real-time chat messaging
- Persistent local user data storage
- Desktop-based GUI interface

## Project Structure

- src/main/java/chatClient - client-side GUI and controllers
- src/main/java/chatServer - server and service logic
- src/main/java/chatProtocol - shared protocol and message models
- src/main/resources - application resources

## Requirements

- Java 11 or newer
- Maven
- MySQL or the configured local database setup used by the project

## Installation and Setup

1. Clone the repository:

```bash
git clone <repository-url>
cd Proyecto2
```

2. Make sure Java and Maven are installed and available in your terminal.

3. Verify the environment variables are configured correctly:

```bash
java -version
mvn -version
```

4. Compile the project:

```bash
mvn -q -DskipTests compile
```

## Run the Application

### Start the server

Open a terminal in the project folder and run:

```bash
java -cp target/classes chatServer.Application
```

### Start the client

Open a second terminal in the project folder and run:

```bash
java -cp "target/classes;target/dependency/*" chatClient.Application
```

## Windows Notes

If you are running the app on Windows, use the same commands in PowerShell. If the classpath does not resolve properly, make sure the compiled classes and dependency jars are present in the target folder.

## Notes

The application uses local XML-based user data and image assets for the current user profile. These files are ignored by Git via the repository ignore rules.
