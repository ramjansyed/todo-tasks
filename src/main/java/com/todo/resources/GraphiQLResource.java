package com.todo.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/graphiql")
@Produces(MediaType.TEXT_HTML)
public class GraphiQLResource {

  @GET
  public Response getGraphiQL() {
    String graphiqlHtml =
        "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "    <title>GraphiQL</title>"
            + "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/graphiql/1.4.2/graphiql.min.css\" />"
            + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/react/17.0.2/umd/react.production.min.js\"></script>"
            + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/react-dom/17.0.2/umd/react-dom.production.min.js\"></script>"
            + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/graphiql/1.4.2/graphiql.min.js\"></script>"
            + "</head>"
            + "<body style=\"margin: 0;\">"
            + "    <div id=\"graphiql\" style=\"height: 100vh;\"></div>"
            + "    <script>"
            + "        function graphQLFetcher(graphQLParams) {"
            + "            return fetch('/graphql', {"
            + "                method: 'POST',"
            + "                headers: { 'Content-Type': 'application/json' },"
            + "                body: JSON.stringify(graphQLParams),"
            + "            }).then(response => response.json());"
            + "        }"
            + "        ReactDOM.render("
            + "            React.createElement(GraphiQL, { fetcher: graphQLFetcher }),"
            + "            document.getElementById('graphiql')"
            + "        );"
            + "    </script>"
            + "</body>"
            + "</html>";

    return Response.ok(graphiqlHtml).build();
  }
}
