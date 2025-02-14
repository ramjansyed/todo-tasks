package com.todo.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ToDo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  String id;

  String title;
  String description;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  List<SubTask> subtasks;
}
