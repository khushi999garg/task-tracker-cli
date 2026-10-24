package com.tasktrackercli;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: task add \"task title\"");
            return;
        }

        TaskService taskService;

        try {
            taskService = new TaskService();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            return;
        }

        switch (args[0]) {
            case "add":
                if (args.length < 2) {
                    System.out.println("Usage: task add \"task title\"");
                } else {
                    Task task;
                    try {
                        task = taskService.addTask(args[1]);
                        System.out.println("Task added: " + task.getTitle() + " (ID: " + task.getId() + ")");
                    } catch (IOException e) {
                        System.out.println("Error adding tasks: " + e.getMessage());
                        return;
                    }
                }
                break;

            case "list":
                if (taskService.listTasks().isEmpty()) {
                    System.out.println("No tasks found.");
                } else {
                    String format = "%-5s %-25s %-10s%n";
                    System.out.format(format, "ID", "TITLE", "STATUS");
                    for (Task task : taskService.listTasks()) {
                        System.out.format(format, task.getId(), task.getTitle(), task.getStatus());
                    }
                }
                break;

            case "done":
                if (args.length < 2) {
                    System.out.println("Usage: task done \"task id\"");
                } else {
                    try {
                        if (taskService.markTaskDone(Integer.parseInt(args[1])))
                            System.out.println("Task updated: " + TaskStatus.DONE);
                        else
                            System.out.println("Task not found");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format. Please enter an integer");
                    } catch (IOException e) {
                        System.out.println("Error updating tasks: " + e.getMessage());
                    }
                }
                break;

            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
