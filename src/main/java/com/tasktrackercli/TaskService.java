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

    public Task addTask(String title) throws IOException {
        List<Task> list = new ArrayList<>(tasks);
        Task task = new Task(id, title);
        list.add(task);
        taskRepository.saveTasks(list);
        tasks.add(task);
        id++;
        try {
            taskRepository.saveNextId(id);
        } catch (IOException e) {
            System.out.println("Couldn't update nextID. " + e.getMessage());
        }
        return task;
    }

    public boolean markTaskDone(int taskId) throws IOException {
        int id = findTask(taskId);

        if (id == -1) {
            return false;
        } else {
            List<Task> list = new ArrayList<>(tasks);
            Task task = new Task(list.get(id).getId(), list.get(id).getTitle(), list.get(id).getStatus());
            task.setStatus(TaskStatus.DONE);
            list.set(id, task);
            taskRepository.saveTasks(list);
            tasks.set(id, task);

            return true;
        }
    }

    public boolean deleteTask(int taskId) throws IOException {
        int id = findTask(taskId);

        if (id == -1) {
            return false;
        } else {
            List<Task> list = new ArrayList<>(tasks);
            list.remove(id);
            taskRepository.saveTasks(list);
            tasks.remove(id);

            return true;
        }
    }

    public List<Task> listTasks() {
        return tasks;
    }

    private void initializeNextId() throws IOException {
        for (Task task : tasks) {
            id = Math.max(id, task.getId());
        }

        id++;

        int nextId = taskRepository.loadNextId();

        if (nextId != -1 && nextId >= id) {
            id = nextId;
        }
        taskRepository.saveNextId(id);
    }

    private int findTask(int taskId) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == taskId) {
                return i;
            }
        }

        return -1;
    }
}
