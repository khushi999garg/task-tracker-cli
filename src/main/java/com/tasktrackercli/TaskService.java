package com.tasktrackercli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private TaskRepository taskRepository = new TaskRepository();
    private List<Task> tasks;
    private int id = 0;

    public TaskService() throws IOException {
        tasks = taskRepository.loadTasks();
        initializeNextId();
    }

    public Task addTask(String title) throws IOException{
        List<Task> list = new ArrayList<>(tasks);
        Task task = new Task(id, title);
        list.add(task);
        taskRepository.saveTasks(list);
        tasks.add(task);
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
