
import com.todo.resources.GraphQLResource;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphQLResourceTest {

    private GraphQL graphQL;
    private GraphQLResource graphQLResource;

    @BeforeEach
    void setUp() {
        graphQL = mock(GraphQL.class);
        graphQLResource = new GraphQLResource(graphQL);
    }

    @Test
    void testExecuteMutation_SuccessfulQuery() {
        // Mock execution result with data
        ExecutionResult executionResult = mock(ExecutionResult.class);
        when(executionResult.getData()).thenReturn(Map.of("key", "value"));
        when(executionResult.getErrors()).thenReturn(List.of());
        when(graphQL.execute(anyString())).thenReturn(executionResult);

        // Prepare request
        Map<String, Object> request = Map.of("query", "mutation { createToDo(title: \"Test\", description: \"Desc\") { id } }");

        // Execute request
        Response response = (Response) graphQLResource.executeMutation(request);

        // Verify
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        assertEquals(Map.of("key", "value"), responseBody.get("data"));
    }

    @Test
    void testExecuteMutation_NullQuery() {
        // Prepare request with null query
        Map<String, Object> request = Map.of();

        // Execute request
        Response response = (Response) graphQLResource.executeMutation(request);

        // Verify
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        assertEquals("Query cannot be null or empty", responseBody.get("error"));
    }

    @Test
    void testExecuteMutation_WithGraphQLErrors() {
        // Mock execution result with errors
        ExecutionResult executionResult = mock(ExecutionResult.class);
        GraphQLError mockError = mock(GraphQLError.class);
        when(mockError.getMessage()).thenReturn("Syntax error in GraphQL query");
        when(executionResult.getErrors()).thenReturn(List.of(mockError));
        when(graphQL.execute(anyString())).thenReturn(executionResult);

        // Prepare request
        Map<String, Object> request = Map.of("query", "mutation { invalidQuery }");

        // Execute request
        Response response = (Response) graphQLResource.executeMutation(request);

        // Verify
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        assertEquals(List.of("Syntax error in GraphQL query"), responseBody.get("errors"));
    }

    @Test
    void testExecuteMutation_InternalServerError() {
        // Mock unexpected exception
        when(graphQL.execute(anyString())).thenThrow(new RuntimeException("Unexpected failure"));

        // Prepare request
        Map<String, Object> request = Map.of("query", "mutation { createToDo(title: \"Test\", description: \"Desc\") { id } }");

        // Execute request
        Response response = (Response) graphQLResource.executeMutation(request);

        // Verify
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Map<String, Object> responseBody = (Map<String, Object>) response.getEntity();
        assertEquals("Internal server error", responseBody.get("error"));
        assertEquals("Unexpected failure", responseBody.get("details"));
    }
}
