# About this Project
This project is a simple ToDo App which facilitates the below actions for both ToDo and SubTasks.
* Create 
* Update 
* Delete
## Dropwizard GraphQL API with GraphiQL UI

This project demonstrates how to set up a GraphQL API in a Java Dropwizard application with a GraphiQL user interface for testing queries.

## Features

- GraphQL API exposed through Dropwizard.
- GraphiQL UI for interactive query testing.

## Prerequisites

- JDK 17 or higher

## Setup

1. **Clone the repository:**

   ```bash
   git clone <repository-url>
   cd <project-directory>
   
2. **Build the project with Gradle:**

    Make sure you have Gradle installed, or use the Gradle wrapper.

   ```bash
    ./gradlew clean build

3. **Run the application:**

    Run the Dropwizard application using the Gradle wrapper or Gradle itself.
    ```bash
   java -jar build/libs/ToDoApp-1.0-SNAPSHOT-all.jar server src/main/resources/config.yml

## Dependencies
This project uses the following key dependencies:

* Dropwizard: Framework for building RESTful web services.

* GraphQL Java: Library to implement GraphQL in Java.

* GraphiQL: Web UI for testing GraphQL queries.** 
  
## GraphQL Endpoint
The GraphQL endpoint is available at:

    GET http://localhost:8080/graphql
    POST http://localhost:8080/graphql

# Postman Validation: 
* execute a GraphQL query, send a POST request with the query parameter in the body.

**Example query** 

    http://localhost:8080/graphql?query={todos{id,title,description,subtasks{id}}}

**Sample Response**
    
    {
        "data": {
            "todos": [
                {
                    "id": "1",
                    "title": "test1",
                    "description": "desc",
                    "subtasks": []
                }
            ]
        }
    }

## GraphiQL UI
To access the GraphiQL UI, navigate to:
You can use the interactive GraphiQL interface to write and execute GraphQL queries directly in your browser.
    
    http://localhost:8080/graphiql

**Example Query in GraphiQL UI**

    query {
        todo(id: "ec047081-e2d0-41fd-abcc-019412a6733c") {
            id
            title
            description
            subtasks {
                id
                title
                description
                todo_id
            }
        }
    }

**Sample Response**

    {
        "data": {
            "todo": {
                "id": "ec047081-e2d0-41fd-abcc-019412a6733c",
                "title": "Learn Dropwizard",
                "description": "Build a GraphQL API",
                    "subtasks": [
                        {
                        "id": "7b3b7704-6381-4a55-ba7c-b79e1c674ea4",
                        "title": "Write Docs",
                        "description": "Update API documentation",
                        "todo_id": "ec047081-e2d0-41fd-abcc-019412a6733c"
                        }
                    ]
                }
            }
    }

## Project Structure
* src/main/java/com/todo
    * Contains the Java source code for the Dropwizard application.
* src/main/resources:
    * Contains configuration files for Dropwizard.
* build.gradle: The Gradle build file, where dependencies and configurations are defined.


    ToDoApp/
        ├── src/
        │   ├── main/
        │   │   ├── java/com/todo/
        │   │   │   ├── ToDoApplication.java
        │   │   │   ├── configuration/
        │   │   │   │   ├── ToDoConfiguration.java
        │   │   │   ├── health/
        │   │   │   │   ├── ToDoHealthCheck.java
        │   │   │   ├── repository/
        │   │   │   │   ├── ToDoRepository.java
        │   │   │   ├── resolvers/
        │   │   │   │   ├── ToDoQueryResolver.java
        │   │   │   │   ├── ToDoMutationResolver.java
        │   │   │   ├── resources/
        │   │   │   │   ├── ToDoResource.java
        │   │   │   ├── models/
        │   │   │   │   ├── ToDo.java
        │   │   │   │   ├── SubTask.java
        │   │   ├── resources/
        │   │   │   ├── schema.graphqls
        │   │   │   ├── config.yml
        ├── build.gradle
        ├── gradlew
        ├── gradlew.bat
        ├── settings.gradle
        ├── README.md



