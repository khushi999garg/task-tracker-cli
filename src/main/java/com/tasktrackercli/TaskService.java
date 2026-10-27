package com.tasktrackercli;

import java.io.IOException;
import java.time.LocalDateTime;
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
        validateTitle(title);
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

    public boolean updateStatus(int taskId, TaskStatus status) throws IOException {
        return update(taskId, null, status);
    }

    public boolean updateTask(int taskId, String title) throws IOException {
        return update(taskId, title, null);
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

    public List<Task> listTasks(TaskStatus status, boolean include) {
        List<Task> list = new ArrayList<>();

        for (Task task : tasks) {
            if (include) {
                if (task.getStatus() == status) {
                    list.add(task);
                }
            } else {
                if (task.getStatus() != status){
                    list.add(task);
                }
            }
        }

        return list;
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

    private boolean update(int taskId, String taskTitle, TaskStatus taskStatus) throws IOException {
        int id = findTask(taskId);

        if (id == -1) {
            return false;
        } else {
            List<Task> list = new ArrayList<>(tasks);
            String title = taskTitle != null ? taskTitle : list.get(id).getTitle();
            TaskStatus status = taskStatus != null ? taskStatus : list.get(id).getStatus();
            LocalDateTime createdAt = list.get(id).getCreatedAt();
            LocalDateTime updatedAt = LocalDateTime.now();

            validateTitle(title);

            Task task = new Task(taskId, title, status, createdAt, updatedAt);

            list.set(id, task);
            taskRepository.saveTasks(list);
            tasks.set(id, task);

            return true;
        }
    }

    private void validateTitle(String title) {
        if (title.trim().isEmpty() || title.trim().equals("null")) {
            throw new IllegalArgumentException("Title can't be empty or null.");
        }
    }
}
