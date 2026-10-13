package com.tasktrackercli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private TaskRepository taskRepository = new TaskRepository();
    private List<Task> tasks;
    private int id = 0;

    public TaskService() {
        try {
            tasks = taskRepository.loadTasks();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            tasks = new ArrayList<>();
        }

        initializeNextId();
    }

    public Task addTask(String title) {
        Task task = new Task(id, title);
        tasks.add(task);
        try {
            taskRepository.saveTasks(tasks);
        } catch (IOException e){
            System.out.println("Error:" + e.getMessage());
        }
        id++;
        return task;
    }

    public List<Task> listTasks() {
        return tasks;
    }

    private void initializeNextId() {
        for (Task task : tasks) {
            id = Math.max(id, task.getId());
        }

        id++;
    }
}
