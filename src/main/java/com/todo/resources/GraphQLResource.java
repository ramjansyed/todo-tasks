package com.todo.resources;

import graphql.GraphQL;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

// GraphQL Resource to handle the query
@Path("/graphql")
public class GraphQLResource {

  private final GraphQL graphQL;

  public GraphQLResource(GraphQL graphQL) {
    this.graphQL = graphQL;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Object executeMutation(Map<String, Object> request) {
    String query = (String) request.get("query");
    if (query == null) {
      throw new IllegalArgumentException("Query can't be null");
    }

    Map<String, Object> result = new HashMap<>();
    result.put("data", graphQL.execute(query).getData());
    return result;
  }

}
