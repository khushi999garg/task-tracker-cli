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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus(){
        return status;
    }
}
