package com.tasktrackercli;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int id = 1;

    public Task addTask(String title) {
        Task task = new Task(id, title);
        tasks.add(task);
        id++;
        return task;
    }

    public List<Task> listTasks() {
        return tasks;
    }
}
