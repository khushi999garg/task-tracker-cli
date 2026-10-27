package com.tasktrackercli;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
        this.status = TaskStatus.TODO;
        LocalDateTime time = LocalDateTime.now();
        this.createdAt = time;
        this.updatedAt = time;
    }

    public Task(int id, String title, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // public void setTitle(String update) {
    //     this.title = update;
    // }

    // public void setStatus(TaskStatus update) {
    //     this.status = update;
    // }
}
