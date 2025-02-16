package com.todo.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomGraphQLErrorHandler implements GraphQLErrorHandler {

  @Override
  public List<GraphQLError> processErrors(List<GraphQLError> errors) {
    return errors.stream()
        .map(
            error -> {
              log.error("GraphQL Error: {}", error.getMessage()); // Log the error message
              return GraphqlErrorBuilder.newError()
                  .message(error.getMessage())
                  .extensions(Map.of("code", getErrorCode(error), "path", error.getPath()))
                  .build();
            })
        .collect(Collectors.toList());
  }

  private String getErrorCode(GraphQLError error) {
    if (error.getMessage().contains("not found")) {
      return "NOT_FOUND";
    } else if (error.getMessage().contains("Invalid input")) {
      return "BAD_REQUEST";
    }
    return "INTERNAL_ERROR";
  }
}
