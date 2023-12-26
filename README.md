# ChaTopApi

This project was generated with [Java] version 17.
This is a CRUD API using Spring Boot, Mysql, JPA

## Getting Started

### 1. Clone the github repository

`git clone https://github.com/cedricfaraud/ChaTopAPI.git`

### 2. Create MySQL Database

Start MySQL, connect to MySQL Command Line or GUI tool.

Run the script below to create the **chatop** database.

`src/main/resouces/script.sql`

### 3. Set the credential of database access of API

edit `/src/main/resources/application.properties`

modify `spring.datasource.username` and `spring.datasource.password` with MySQL user/password.

### 4. Run the app using maven

`mvn spring-boot:run`
The application will start running at http://localhost:3001.

# Documentation

When the Api is running, the documentation is available at :

HTML format :
http://localhost:3001/swagger-ui/index.html

Json format :
http://localhost:3001/v3/api-docs

