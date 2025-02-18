package com.todo.exceptions;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;

import java.util.List;
import java.util.stream.Collectors;


public class CustomGraphQLErrorHandler implements GraphQLErrorHandler {

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        return errors.stream()
                .collect(Collectors.toList());
    }

    private GraphQLError handleError(GraphQLError error) {
        // Customize error handling logic here
        return new GenericGraphQLError(error.getMessage());
    }
}