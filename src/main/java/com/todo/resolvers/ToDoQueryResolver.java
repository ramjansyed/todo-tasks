package com.todo.resolvers;

import com.todo.models.SubTask;
import com.todo.models.ToDo;
import com.todo.repository.ToDoRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;

public class ToDoQueryResolver implements GraphQLQueryResolver {
  private final ToDoRepository repository;

  public ToDoQueryResolver(ToDoRepository repository) {
    this.repository = repository;
  }

  // Fetches the list of all To-Do objects
  public List<ToDo> todos() {
    return repository.getAllTodos();
  }

  // Fetches a single To-Do by id
  public ToDo todo(String id) {
    return repository.getToDoById(id);
  }

  // Fetches the list of all SubTask objects
  public List<SubTask> subtasks() {
    return repository.getAllSubTasks();
  }

  // Fetches the list of all SubTask for particular todo
  public List<SubTask> subtaskbytodoid(String todoId) {
    return repository.getSubTasksByToDoId(todoId);
  }

  // Fetches a subtask by id
  public SubTask subtaskbyid(String id) {
    return repository.getSubTaskById(id);
  }
}
