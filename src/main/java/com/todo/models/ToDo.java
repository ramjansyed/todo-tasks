package com.todo.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ToDo(
    String id,
    @Valid @NotBlank(message = "Title cannot be blank") String title,
    String description,
    Boolean completed,
    List<SubTask> subtasks) {
}
