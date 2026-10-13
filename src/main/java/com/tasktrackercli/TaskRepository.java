package com.tasktrackercli;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    public List<Task> loadTasks() throws IOException {
        File file = new File("tasks.json");

        if (!file.exists()) {
            file.createNewFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            }
            return new ArrayList<>();
        }

        Path filePath = Path.of("tasks.json");
        String content = Files.readString(filePath);

        if (content.trim().equals("[]") || content.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();

        int objectStart = content.indexOf("{");

        while (objectStart != -1) {
            int objectEnd = content.indexOf("}", objectStart);
            String taskJson = content.substring(objectStart, objectEnd + 1);

            int idIndex = taskJson.indexOf("\"id\"");
            int idColonIndex = taskJson.indexOf(":", idIndex);
            int idCommaIndex = taskJson.indexOf(",", idColonIndex);
            int id = Integer.parseInt(taskJson.substring(idColonIndex + 1, idCommaIndex).trim());

            int titleIndex = taskJson.indexOf("\"title\"");
            int titleColonIndex = taskJson.indexOf(":", titleIndex);
            int titleStart = taskJson.indexOf("\"", titleColonIndex);
            int titleEnd = findClosingQuote(taskJson, titleStart + 1);
            String title = taskJson.substring(titleStart + 1, titleEnd);

            int statusIndex = taskJson.indexOf("\"status\"");
            int statusColonIndex = taskJson.indexOf(":", statusIndex);
            int statusStart = taskJson.indexOf("\"", statusColonIndex);
            int statusEnd = taskJson.indexOf("\"", statusStart + 1);
            TaskStatus status = TaskStatus.valueOf(taskJson.substring(statusStart + 1, statusEnd));

            Task task = new Task(id, title, status);
            tasks.add(task);

            objectStart = content.indexOf("{", objectEnd + 1);
        }

        return tasks;
    }

    public void saveTasks(List<Task> tasks) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        int i = 0;

        for (Task task : tasks) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append("{\"id\": ");
            sb.append(String.valueOf(task.getId()));
            sb.append(", \"title\": \"");
            sb.append(escapeJson(task.getTitle()));
            sb.append("\", \"status\": \"");
            sb.append(task.getStatus());
            sb.append("\"}");
            i++;
        }

        sb.append("]");

        // Path filePath = Path.of("tasks.json");
        File file = new File("tasks.json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(sb.toString());
        }
    }

    private String escapeJson(String text) {
        text = text.replace("\\", "\\\\");
        text = text.replace("\"", "\\\"");
        text = text.replace("\n", "\\n");
        text = text.replace("\t", "\\t");
        return text;
    }

    private int findClosingQuote(String text, int start) {
        int n = text.length();
        boolean skip = false;
        for (int i = start; i < n; i++) {
            if (skip) {
                skip = false;
                continue;
            }

            if (text.charAt(i) == '\\') {
                skip = true;
            } else if (text.charAt(i) == '\"') {
                return i;
            }
        }

        return -1;
    }
}
