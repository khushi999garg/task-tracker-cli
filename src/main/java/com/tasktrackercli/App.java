package com.tasktrackercli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                if (args.length != 2) {
                    System.out.println("Usage: task add \"task title\"");
                } else {
                    Task task;
                    try {
                        task = taskService.addTask(args[1]);
                        System.out.println("Task added: " + task.getTitle() + " (ID: " + task.getId() + ")");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Error adding tasks: " + e.getMessage());
                        return;
                    }
                }
                break;

            case "list":
                List<Task> tasks = new ArrayList<>();
                if (args.length > 2) {
                    System.out.println("Usage: task list [status]");
                    break;
                } else {
                    if (args.length == 1) {
                        tasks = taskService.listTasks();
                        if (tasks.isEmpty()) {
                            System.out.println("No tasks found.");
                            break;
                        }

                    } else if (args.length == 2) {
                        try {
                            if (args[1].equals("NOT_DONE")) {
                                tasks = taskService.listTasks(TaskStatus.DONE, false);
                            } else {
                                tasks = taskService.listTasks(TaskStatus.valueOf(args[1]), true);
                            }
                            if (tasks.isEmpty()) {
                                System.out.println("No tasks found.");
                                break;
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid Input error: " + e.getMessage());
                            break;
                        }

                    }

                    String format = "%-5s %-25s %-10s%n";
                    System.out.format(format, "ID", "TITLE", "STATUS");
                    for (Task task : tasks) {
                        System.out.format(format, task.getId(), task.getTitle(), task.getStatus());
                    }
                }
                break;

            case "mark-done":
                if (args.length != 2) {
                    System.out.println("Usage: task mark-done <id>");
                } else {
                    try {
                        if (taskService.updateStatus(Integer.parseInt(args[1]), TaskStatus.DONE))
                            System.out.println("Task updated: " + TaskStatus.DONE);
                        else
                            System.out.println("Task not found");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format. Please enter an integer");
                    } catch (IOException e) {
                        System.out.println("Error updating task: " + e.getMessage());
                    }
                }
                break;

            case "mark-in-progress":
                if (args.length != 2) {
                    System.out.println("Usage: task mark-in-progress <id>");
                } else {
                    try {
                        if (taskService.updateStatus(Integer.parseInt(args[1]), TaskStatus.IN_PROGRESS))
                            System.out.println("Task updated: " + TaskStatus.IN_PROGRESS);
                        else
                            System.out.println("Task not found");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format. Please enter an integer");
                    } catch (IOException e) {
                        System.out.println("Error updating task: " + e.getMessage());
                    }
                }
                break;

            case "delete":
                if (args.length != 2) {
                    System.out.println("Usage: task delete <id>");
                } else {
                    try {
                        if (taskService.deleteTask(Integer.parseInt(args[1])))
                            System.out.println("Task deleted successfully");
                        else
                            System.out.println("Task not found.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format. Please enter an integer");
                    } catch (IOException e) {
                        System.out.println("Error deleting tasks: " + e.getMessage());
                    }
                }
                break;

            case "update":
                if (args.length != 3) {
                    System.out.println("Usage: task update <id> \"task title\"");
                } else {
                    try {
                        if (taskService.updateTask(Integer.parseInt(args[1]), args[2]))
                            System.out.println("Task updated successfully.");
                        else
                            System.out.println("Task not found.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format. Please enter an integer as ID");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Error updating task: " + e.getMessage());
                    }
                }
                break;

            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
