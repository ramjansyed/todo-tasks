package com.todo.resolvers;

import com.todo.models.SubTask;
import com.todo.models.SubTaskInput;
import com.todo.models.ToDo;
import com.todo.repository.ToDoRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ToDoMutationResolver implements GraphQLMutationResolver {
  ToDoRepository repository;

  public ToDo createToDo(String title, String description, List<SubTaskInput> subTaskInputList) {
    return repository.createToDo(title, description, subTaskInputList);
  }

  public ToDo updateToDo(String id, String title, String description) {
    return repository.updateToDo(id, title, description);
  }

  public boolean deleteToDo(String id) {
    return repository.deleteToDo(id);
  }

  public SubTask createSubTask(String ToDoId, String title, String description) {
    return repository.createSubTask(ToDoId, title, description);
  }

  public SubTask updateSubTask(String id, String title, String description) {
    return repository.updateSubTask(id, title, description);
  }

  public boolean deleteSubTask(String id) {
    return repository.deleteSubTask(id);
  }
}
