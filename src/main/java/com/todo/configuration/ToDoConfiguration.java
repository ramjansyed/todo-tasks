package com.todo.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ToDoConfiguration extends Configuration {

  @Valid @NotNull private DataSourceFactory database = new DataSourceFactory();

  @JsonProperty("database")
  public DataSourceFactory getDatabase() {
    return database;
  }
}
