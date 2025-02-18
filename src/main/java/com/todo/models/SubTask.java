package com.todo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public record SubTask(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) String id,
    String title,
    String description,
    Boolean completed,
    String todo_id) {
  public SubTask {
    if (completed == null) {
      completed = false;
    }
  }
}
