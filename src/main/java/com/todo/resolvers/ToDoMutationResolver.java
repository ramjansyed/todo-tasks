package com.todo.resolvers;

import com.todo.models.SubTask;
import com.todo.models.SubTaskInput;
import com.todo.models.ToDo;
import com.todo.repository.ToDoRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ToDoMutationResolver implements GraphQLMutationResolver {
  ToDoRepository repository;

  public ToDo createToDo(
      String title, String description, Boolean completed, List<SubTaskInput> subTaskInputList) {
    return repository.createToDo(title, description, completed, subTaskInputList);
  }

  public ToDo updateToDo(String id, String title, String description, Boolean completed) {
    return repository.updateToDo(id, title, description, completed);
  }

  public boolean deleteToDo(String id) {
    return repository.deleteToDo(id);
  }

  public SubTask createSubTask(String ToDoId, String title, String description, Boolean completed) {
    return repository.createSubTask(ToDoId, title, description, completed);
  }

  public SubTask updateSubTask(String id, String title, String description, Boolean completed) {
    return repository.updateSubTask(id, title, description, completed);
  }

  public boolean deleteSubTask(String id) {
    return repository.deleteSubTask(id);
  }
}
