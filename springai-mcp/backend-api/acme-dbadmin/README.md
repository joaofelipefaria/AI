# acme-db

## Overview
**acme-db** is a database management project designed to handle the creation and maintenance of the database for the **acme** application. It provides operations to create tables and associated objects such as sequences. The project configuration allows you to specify the desired operation directly in the **pom.xml** file.

## Features
- Creation of database tables and related objects (e.g., sequences)
- Easy configuration of database operations via Maven
- Supports structured database setup and initialization

## Usage
### Configuring Operations
The operations for managing the database are configured in the **pom.xml** file. You can specify which operation to execute by modifying the relevant settings.

### Running the Database Setup
To execute the database operations, use the following Maven command:
```sh
mvn exec:java
```
Ensure that the appropriate settings are configured in **pom.xml** before running this command.

## Requirements
- Java (JDK version compatible with Maven)
- Maven
- Database system compatible with the **acme** application