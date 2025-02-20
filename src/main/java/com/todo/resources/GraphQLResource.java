package com.todo.resources;

import graphql.ExecutionResult;
import graphql.GraphQL;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

// GraphQL Resource to handle the query
@Path("/graphql")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GraphQLResource {

  GraphQL graphQL;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Object executeMutation(Map<String, Object> request) {

    try {
      String query = (String) request.get("query");
      if (query == null || query.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Query cannot be null or empty"))
                .build();
      }

      ExecutionResult executionResult = graphQL.execute(query);

      System.out.println("Errors: " + executionResult.getErrors());

      // Handle errors in the execution result
      if (!executionResult.getErrors().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("errors", executionResult.getErrors()
                        .stream()
                        .map(error -> error.getMessage()) // Extract only the message
                        .toList()))
                .build();
      }


      Map<String, Object> result = new HashMap<>();
      result.put("data", executionResult.getData());
      return Response.ok(result).build();

    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity(Map.of("error", "Internal server error", "details", e.getMessage()))
              .build();
    }
  }

}
