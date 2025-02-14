// package com.todo.resources;
//
//
// import com.todo.models.ToDo;
//
// import javax.ws.rs.*;
// import javax.ws.rs.core.MediaType;
// import java.util.ArrayList;
// import java.util.List;
//
// @Path("/graphql")
// @Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON)
// public class ToDoResource {
//
//    private final List<ToDo> todos = new ArrayList<>();
//
//    @GET
//    public List<ToDo> getTodo() {
//        System.out.println("inside getTodos");
//        ToDo todo = new ToDo("1","test1","desc",new ArrayList<>());
//        List<ToDo> toDoList = new ArrayList<>();
//        toDoList.add(todo);
//        return toDoList;
//    }
//
//    @POST
//    public void addTodo(ToDo toDo) {
//        todos.add(toDo);
//    }
// }
