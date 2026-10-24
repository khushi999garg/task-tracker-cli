package com.tasktrackercli;

public class Task {
    private int id;
    private String title;
    private TaskStatus status;

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
        this.status = TaskStatus.TODO;
    }

    public Task(int id, String title, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setTitle(String update) {
        this.title = update;
    }
    public void setStatus(TaskStatus update) {
        this.status = update;
    }
}
