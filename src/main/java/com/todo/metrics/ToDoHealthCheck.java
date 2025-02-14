package com.todo.metrics;

import com.codahale.metrics.health.HealthCheck;

public class ToDoHealthCheck extends HealthCheck {
  @Override
  protected Result check() {
    return Result.healthy("Service is running smoothly!");
  }
}
