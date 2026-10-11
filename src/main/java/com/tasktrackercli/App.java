package com.tasktrackercli;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: task add \"task title\"");
            return;
        }

        TaskService taskService = new TaskService();

        if (args[0].equals("add")) {
            if (args.length < 2) {
                System.out.println("Usage: task add \"task title\"");
            } else {
                Task task = taskService.addTask(args[1]);
                System.out.println("Task added: " + task.getTitle() + " (ID: " + task.getId() + ")");
            }
        } else if (args[0].equals("list")) {
            if (taskService.listTasks().isEmpty()) {
                System.out.println("No tasks found.");
            } else {
                String format = "%-5s %-25s %-10s%n";
                System.out.format(format, "ID", "TITLE", "STATUS");
                for (Task task : taskService.listTasks()) {
                    System.out.format(format, task.getId(), task.getTitle(), task.getStatus());
                }
            }
        } else {
            System.out.println("Unknown command");
        }
    }
}
