package com.todo;

import com.todo.configuration.ToDoConfiguration;
import com.todo.metrics.ToDoHealthCheck;
import com.todo.repository.ToDoRepository;
import com.todo.resolvers.ToDoMutationResolver;
import com.todo.resolvers.ToDoQueryResolver;
import com.todo.resources.GraphiQLResource;
import com.todo.exceptions.CustomGraphQLErrorHandler;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import javax.servlet.ServletRegistration;
import org.jdbi.v3.core.Jdbi;

public class ToDoApplication extends Application<ToDoConfiguration> {

  public static void main(String[] args) throws Exception {
    new ToDoApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<ToDoConfiguration> bootstrap) {
    // Any additional initialization if needed
  }

  @Override
  public void run(ToDoConfiguration configuration, Environment environment) {

    // Initialize JDBI
    Jdbi jdbi = Jdbi.create(configuration.getDatabase().getUrl());

    ToDoRepository toDoRepository = new ToDoRepository(jdbi);
    ToDoQueryResolver queryResolver = new ToDoQueryResolver(toDoRepository);
    ToDoMutationResolver mutationResolver = new ToDoMutationResolver(toDoRepository);

    // Create GraphQL schema
    GraphQLSchema schema =
        SchemaParser.newParser()
            .file("schema.graphqls")
            .resolvers(queryResolver, mutationResolver)
            .build()
            .makeExecutableSchema();

    // Register GraphQL servlet
    ServletRegistration.Dynamic graphQLServlet =
        environment.servlets().addServlet("GraphQLServlet", GraphQLHttpServlet.with(schema));
    graphQLServlet.addMapping("/graphql");

    //  environment.jersey().register(new ToDoResource()); // Register Resource for REST API
    // endpoints
    final ToDoHealthCheck toDoHealthCheck = new ToDoHealthCheck();
    environment.healthChecks().register("healthCheck", toDoHealthCheck);
    environment.jersey().register(new GraphiQLResource()); // Register GraphiQL
    environment.jersey().register(new CustomGraphQLErrorHandler()); // Register ErrorHandler
  }
}
