package com.todo.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public record ToDo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) String id,
    String title,
    String description,
    Boolean completed,
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) List<SubTask> subtasks) {
  public ToDo {
    if (completed == null) {
      completed = false;
    }
  }
}
