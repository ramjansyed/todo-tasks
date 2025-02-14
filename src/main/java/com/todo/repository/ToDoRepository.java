package com.todo.repository;

import com.todo.exceptions.InvalidInputException;
import com.todo.models.SubTask;
import com.todo.models.SubTaskInput;
import com.todo.models.ToDo;
import com.todo.exceptions.TodoNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

@Slf4j
public class ToDoRepository {
  private final Jdbi jdbi;

  public ToDoRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
    setupDatabase();
  }

  private void setupDatabase() {
    try (Handle handle = jdbi.open()) {
      handle.execute(
          "CREATE TABLE IF NOT EXISTS todo ("
              + "id TEXT PRIMARY KEY, "
              + "title TEXT NOT NULL, "
              + "description TEXT)");

      handle.execute(
          "CREATE TABLE IF NOT EXISTS subtask ("
              + "id TEXT PRIMARY KEY, "
              + "title TEXT, "
              + "description TEXT, "
              + "todo_id TEXT NOT NULL, "
              + "FOREIGN KEY(todo_id) REFERENCES todo(id) ON DELETE CASCADE)");
    }
  }

  public List<ToDo> getAllTodos() {
    return jdbi.withHandle(
        handle ->
            handle
                .createQuery("SELECT * FROM todo")
                .map(
                    (rs, ctx) ->
                        new ToDo(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            getSubTasksByToDoId(rs.getString("id")) // Fetch subtasks for each To-Do
                            ))
                .list());
  }

  public List<SubTask> getAllSubTasks() {
    return jdbi.withHandle(
            handle ->
                    handle
                            .createQuery("SELECT * FROM subtask")
                            .map(
                                    (rs, ctx) ->
                                            new SubTask(
                                                    rs.getString("id"),
                                                    rs.getString("title"),
                                                    rs.getString("description"),
                                                    rs.getString("todo_id")
                                            ))
                            .list());
  }

  public ToDo getToDoById(String id) {
    if (id == null || id.isBlank()) {
      throw new InvalidInputException("id is empty or null");
    }
    return jdbi.withHandle(
        handle ->
            handle
                .createQuery("SELECT * FROM todo WHERE id = ?")
                .bind(0, id)
                .map(
                    (rs, ctx) ->
                        new ToDo(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            getSubTasksByToDoId(rs.getString("id")) // Fetch subtasks for this ToDo
                            ))
                .findOne() // Ensures only one result is returned
                .orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not found")));
  }

  public ToDo createToDo(String title, String description, List<SubTaskInput> subTaskInputList) {
    if (title == null || title.isBlank()) {
      throw new InvalidInputException("title is empty or null");
    }
    String id = UUID.randomUUID().toString();
    jdbi.withHandle(
        handle ->
            handle
                .createUpdate("INSERT INTO todo (id, title, description) VALUES (?, ?, ?)")
                .bind(0, id)
                .bind(1, title)
                .bind(2, description)
                .execute());

    List<SubTask> createdSubTasks = new ArrayList<>();

    if (subTaskInputList != null && !subTaskInputList.isEmpty()) {
      for (SubTaskInput subTask : subTaskInputList) {
        SubTask createdSubTask = insertSubTask(id, subTask);
        createdSubTasks.add(createdSubTask);
      }
    }
    return new ToDo(id, title, description, createdSubTasks);
  }

  private SubTask insertSubTask(String todoId, SubTaskInput subTaskInput) {
    String subTaskId = UUID.randomUUID().toString();

    jdbi.withHandle(handle ->
            handle.createUpdate("INSERT INTO subtask (id, title, description, todo_id) VALUES (?, ?, ?, ?)")
                    .bind(0, subTaskId)
                    .bind(1, subTaskInput.getTitle())
                    .bind(2, subTaskInput.getDescription()) // SQLite uses INTEGER for boolean
                    .bind(3, todoId)
                    .execute()
    );

    return new SubTask(subTaskId, subTaskInput.getTitle(), subTaskInput.getDescription(),todoId);
  }

  public ToDo updateToDo(String id, String title, String description) {
    if (title == null || title.isBlank() || id == null || id.isBlank()) {
      throw new InvalidInputException("id or title or both empty or null");
    }
    jdbi.withHandle(
        handle ->
            handle
                .createUpdate("UPDATE todo SET title = ?, description = ? WHERE id = ?")
                .bind(0, title)
                .bind(1, description)
                .bind(2, id)
                .execute());
    return new ToDo(id, title, description, getSubTasksByToDoId(id)); // Return updated ToDo
  }

  public boolean deleteToDo(String id) {
    if ( id == null || id.isBlank()) {
      throw new InvalidInputException("id is empty or null");
    }
    return jdbi.withHandle(
        handle ->
            handle.createUpdate("DELETE FROM todo WHERE id = ?").bind(0, id).execute()
                > 0 // Returns true if at least 1 row was deleted
        );
  }

  public List<SubTask> getSubTasksByToDoId(String todoId) {
    if ( todoId == null || todoId.isBlank()) {
      throw new InvalidInputException("todoId is empty or null");
    }
    return jdbi.withHandle(
        handle ->
            handle
                .createQuery("SELECT * FROM subtask WHERE todo_id = ?")
                .bind(0, todoId)
                .map(
                    (rs, ctx) ->
                        new SubTask(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            todoId))
                .list());
  }

  public SubTask getSubTaskById(String id) {
    if ( id == null || id.isBlank()) {
      throw new InvalidInputException("todoId is empty or null");
    }
    return jdbi.withHandle(
            handle ->
                    handle
                            .createQuery("SELECT * FROM subtask WHERE id = ?")
                            .bind(0, id)
                            .map(
                              (rs, ctx) ->
                                      new SubTask(
                                              id,
                                              rs.getString("title"),
                                              rs.getString("description"),
                                              rs.getString("id")) // Fetch subtasks for this ToDo
                                      )
                            .findOne() // Ensures only one result is returned
                            .orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not found")));
  }

  public SubTask createSubTask(String todoId, String title, String description) {
    if (title == null || title.isBlank() || todoId == null || todoId.isBlank()) {
      throw new InvalidInputException("id or title or both empty or null");
    }
    String id = UUID.randomUUID().toString();
    jdbi.withHandle(
        handle ->
            handle
                .createUpdate(
                    "INSERT INTO subtask (id, todo_id, title, description) VALUES (?, ?, ?, ?)")
                .bind(0, id)
                .bind(1, todoId)
                .bind(2, title)
                .bind(3, description)
                .execute());
    return new SubTask(id, title, description, todoId); // Return newly created subtask
  }

  public SubTask updateSubTask(String id, String title, String description) {
    if (title == null || title.isBlank() || id == null || id.isBlank()) {
      throw new InvalidInputException("id or title or both empty or null");
    }
    jdbi.withHandle(
        handle ->
            handle
                .createUpdate("UPDATE subtask SET title = ?, description = ? WHERE id = ?")
                .bind(0, title)
                .bind(1, description)
                .bind(2, id)
                .execute());
    return new SubTask(id, description, title, getSubTaskTodoId(id)); // Return updated subtask
  }

  public boolean deleteSubTask(String id) {
    if ( id == null || id.isBlank()) {
      throw new InvalidInputException("id is empty or null");
    }
    return jdbi.withHandle(
        handle ->
            handle.createUpdate("DELETE FROM subtask WHERE id = ?").bind(0, id).execute()
                > 0 // Returns true if at least 1 row was deleted
        );
  }

  // Helper method
  private String getSubTaskTodoId(String id) {
    if ( id == null || id.isBlank()) {
      throw new InvalidInputException("id is empty or null");
    }
    return jdbi.withHandle(
        handle ->
            handle
                .createQuery("SELECT todo_id FROM subtask WHERE id = ?")
                .bind(0, id)
                .mapTo(String.class)
                .one() // Should return a single result
        );
  }

}
