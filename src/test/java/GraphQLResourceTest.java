import com.todo.ToDoApplication;
import com.todo.configuration.ToDoConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphQLResourceTest {

    @RegisterExtension
    public static final DropwizardAppExtension<ToDoConfiguration> EXT =
            new DropwizardAppExtension<>(ToDoApplication.class, ResourceHelpers.resourceFilePath("config-test.yml"));

    @Test
    public void testGraphQLQuery() {
        String query = "{ \"query\": \"{ todos { id title } }\" }";
        Client client = EXT.client();
        Response response = client.target("http://localhost:8082/graphql")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(query));

        assertEquals(200, response.getStatus());
        // Add more assertions to validate the response
    }
}