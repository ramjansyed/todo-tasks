package com.todo.repository;

import com.todo.exceptions.InvalidInputException;
import com.todo.exceptions.TodoNotFoundException;
import com.todo.models.SubTask;
import com.todo.models.SubTaskInput;
import com.todo.models.ToDo;
import com.todo.utils.InputSanitizer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ToDoRepository {
    private final Jdbi jdbi;

    public ToDoRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
        setupDatabase();
    }

    private void setupDatabase() {
        try (Handle handle = jdbi.open()) {
            log.info("Setting up database tables if they do not exist.");
            handle.execute(
                    "CREATE TABLE IF NOT EXISTS todo ("
                            + "id TEXT PRIMARY KEY, "
                            + "title TEXT NOT NULL, "
                            + "description TEXT, "
                            + "completed BOOLEAN)");

            handle.execute(
                    "CREATE TABLE IF NOT EXISTS subtask ("
                            + "id TEXT PRIMARY KEY, "
                            + "title TEXT, "
                            + "description TEXT, "
                            + "completed BOOLEAN, "
                            + "todo_id TEXT NOT NULL, "
                            + "FOREIGN KEY(todo_id) REFERENCES todo(id) ON DELETE CASCADE)");
            log.info("Database setup completed.");
        } catch (Exception e) {
            log.error("Error setting up database: {}", e.getMessage(), e);
        }
    }

    public List<ToDo> getAllTodos() {
        log.info("Fetching all todos from the database.");
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
                                                        rs.getBoolean("completed"),
                                                        getSubTasksByToDoId(rs.getString("id"))))
                                .list());
    }

    public ToDo getToDoById(String id) {
        log.info("Fetching todo with ID: {}", id);
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
                                                        rs.getBoolean("completed"),
                                                        getSubTasksByToDoId(rs.getString("id"))))
                                .findOne()
                                .orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not found")));
    }

    public ToDo createToDo(
            @Valid @NotBlank String title,
            String description,
            Boolean completed,
            List<SubTaskInput> subTaskInputList) {

        System.out.println("Creating todo with title: " + title);
        // Sanitize the input
        String sanitizedTitle = InputSanitizer.sanitize(title);
        String sanitizedDescription = InputSanitizer.sanitize(description);

        String id = UUID.randomUUID().toString();
        jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate(
                                        "INSERT INTO todo (id, title, description, completed) VALUES (?, ?, ?, ?)")
                                .bind(0, id)
                                .bind(1, sanitizedTitle)
                                .bind(2, sanitizedDescription)
                                .bind(3, completed != null ? completed : false)
                                .execute());

        List<SubTask> createdSubTasks = new ArrayList<>();

        if (subTaskInputList != null && !subTaskInputList.isEmpty()) {
            for (SubTaskInput subTask : subTaskInputList) {
                SubTask createdSubTask = insertSubTask(id, subTask);
                createdSubTasks.add(createdSubTask);
            }
        }

        log.info("Created todo with ID: {}", id);

        return new ToDo(id, title, description, completed != null ? completed : false, createdSubTasks);
    }

    public ToDo updateToDo(@NotNull @NotBlank String id, String title, String description, Boolean completed) {

        // Sanitize the input
        String sanitizedTitle = InputSanitizer.sanitize(title);
        String sanitizedDescription = InputSanitizer.sanitize(description);

        log.info("Updating todo with ID: {}", id);

        jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate(
                                        "UPDATE todo SET title = ?, description = ?, completed = ? WHERE id = ?")
                                .bind(0, sanitizedTitle)
                                .bind(1, sanitizedDescription)
                                .bind(2, completed)
                                .bind(3, id)
                                .execute());
        return new ToDo(id, title, description, completed, getSubTasksByToDoId(id));
    }

    public boolean deleteToDo(String id) {
        log.info("Deleting todo with ID: {}", id);

        if (id == null || id.isBlank()) {
            throw new InvalidInputException("id is empty or null");
        }
        return jdbi.withHandle(
                handle -> handle.createUpdate("DELETE FROM todo WHERE id = ?").bind(0, id).execute() > 0);
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
                                                        rs.getBoolean("completed"),
                                                        rs.getString("todo_id")))
                                .list());
    }

    public List<SubTask> getSubTasksByToDoId(String todoId) {
        log.info("Fetching subtasks with todoId: {}", todoId);

        if (todoId == null || todoId.isBlank()) {
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
                                                        rs.getBoolean("completed"),
                                                        todoId))
                                .list());
    }

    public SubTask getSubTaskById(String id) {
        log.info("Fetching subtasks with id: {}", id);

        if (id == null || id.isBlank()) {
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
                                                        rs.getBoolean("completed"),
                                                        rs.getString("todo_id")))
                                .findOne()
                                .orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not found")));
    }

    public SubTask createSubTask(String todoId, String title, String description, Boolean completed) {
        if (title == null || title.isBlank() || todoId == null || todoId.isBlank()) {
            throw new InvalidInputException("id or title or both empty or null");
        }
        // Sanitize the input
        String sanitizedTitle = InputSanitizer.sanitize(title);
        String sanitizedDescription = InputSanitizer.sanitize(description);

        String id = UUID.randomUUID().toString();
        jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate(
                                        "INSERT INTO subtask (id, todo_id, title, description, completed) VALUES (?, ?, ?, ?, ?)")
                                .bind(0, id)
                                .bind(1, todoId)
                                .bind(2, sanitizedTitle)
                                .bind(3, sanitizedDescription)
                                .bind(4, completed != null ? completed : false)
                                .execute());
        log.info("Created subtasks with id: {} and todoId: {}", id, todoId);

        return new SubTask(id, title, description, completed != null ? completed : false, todoId);
    }

    public SubTask updateSubTask(String id, String title, String description, Boolean completed) {
        log.info("Updating subtasks with id: {} and title: {}", id, title);

        if (id == null || id.isBlank()) {
            throw new InvalidInputException("id is empty or null");
        }
        // Sanitize the input
        String sanitizedTitle = InputSanitizer.sanitize(title);
        String sanitizedDescription = InputSanitizer.sanitize(description);

        jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate(
                                        "UPDATE subtask SET title = ?, description = ?, completed = ? WHERE id = ?")
                                .bind(0, sanitizedTitle)
                                .bind(1, sanitizedDescription)
                                .bind(2, completed)
                                .bind(3, id)
                                .execute());
        return new SubTask(id, title, description, completed, getSubTaskTodoId(id));
    }

    public boolean deleteSubTask(String id) {
        log.info("Deleting subtasks with id: {}", id);

        if (id == null || id.isBlank()) {
            throw new InvalidInputException("id is empty or null");
        }
        return jdbi.withHandle(
                handle ->
                        handle.createUpdate("DELETE FROM subtask WHERE id = ?").bind(0, id).execute() > 0);
    }

    // Helper methods

    private SubTask insertSubTask(String todoId, SubTaskInput subTaskInput) {
        String subTaskId = UUID.randomUUID().toString();

        jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate(
                                        "INSERT INTO subtask (id, title, description, completed, todo_id) VALUES (?, ?, ?, ?, ?)")
                                .bind(0, subTaskId)
                                .bind(1, subTaskInput.title())
                                .bind(2, subTaskInput.description())
                                .bind(3, false)
                                .bind(4, todoId)
                                .execute());

        return new SubTask(subTaskId, subTaskInput.title(), subTaskInput.description(), false, todoId);
    }

    private String getSubTaskTodoId(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidInputException("id is empty or null");
        }
        return jdbi.withHandle(
                handle ->
                        handle
                                .createQuery("SELECT todo_id FROM subtask WHERE id = ?")
                                .bind(0, id)
                                .mapTo(String.class)
                                .one());
    }
}