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


    mutation {
        createToDo(
            title: "Plan Project"
            description: "Define tasks and milestones"
            subTasks: [
                { title: "Research GraphQL", description: "test" }
                { title: "Set up Dropwizard", description: "test" }
                ]
            ) {
                id
                title
                description
                subtasks {
                    id
                    title
                    todo_id
                }
            }
        }

**Sample Response**

        {
            "data": {
                "createToDo": {
                    "id": "42807759-c113-4ebc-8d4f-59d8b4e77008",
                    "title": "Plan Project",
                    "description": "Define tasks and milestones",
                        "subtasks": [
                        {
                            "id": "3fb33684-76a7-42a9-bf5d-39d7359a1719",
                            "title": "Research GraphQL",
                            "todo_id": "42807759-c113-4ebc-8d4f-59d8b4e77008"
                        },
                        {
                            "id": "567e8984-64fc-42ba-bc0e-350a2b57af11",
                            "title": "Set up Dropwizard",
                            "todo_id": "42807759-c113-4ebc-8d4f-59d8b4e77008"
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



