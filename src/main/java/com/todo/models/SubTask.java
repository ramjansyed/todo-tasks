package com.todo.models;

public record SubTask(
    String id,
    String title,
    String description,
    Boolean completed,
    String todo_id) {
}
