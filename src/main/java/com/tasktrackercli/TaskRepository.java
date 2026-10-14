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

            if (objectEnd == -1) {
                throw new IOException("Invalid JSON: missing closing brace");
            }

            String taskJson = content.substring(objectStart, objectEnd + 1);

            int idIndex = taskJson.indexOf("\"id\"");

            if (idIndex == -1) {
                throw new IOException("Invalid JSON: missing task id");
            }

            int idColonIndex = taskJson.indexOf(":", idIndex);

            if (idColonIndex == -1) {
                throw new IOException("Invalid JSON");
            }

            int idCommaIndex = taskJson.indexOf(",", idColonIndex);

            if (idCommaIndex == -1) {
                throw new IOException("Invalid JSON");
            }

            int id;

            try {
                id = Integer.parseInt(taskJson.substring(idColonIndex + 1, idCommaIndex).trim());
            } catch (NumberFormatException e) {
                throw new IOException("Invalid JSON: task id must be an integer");
            }

            int titleIndex = taskJson.indexOf("\"title\"");

            if (titleIndex == -1) {
                throw new IOException("Invalid JSON: missing task title");
            }

            int titleColonIndex = taskJson.indexOf(":", titleIndex);

            if (titleColonIndex == -1) {
                throw new IOException("Invalid JSON: malformed task title");
            }

            int titleStart = taskJson.indexOf("\"", titleColonIndex);

            if (titleStart == -1) {
                throw new IOException("Invalid JSON: malformed task title");
            }

            int titleEnd = findClosingQuote(taskJson, titleStart + 1);

            if (titleEnd == -1) {
                throw new IOException("Invalid JSON: missing closing quote in task title");
            }

            String title = unescapeJson(taskJson.substring(titleStart + 1, titleEnd));

            int statusIndex = taskJson.indexOf("\"status\"");

            if (statusIndex == -1) {
                throw new IOException("Invalid JSON: missing task status");
            }

            int statusColonIndex = taskJson.indexOf(":", statusIndex);

            if (statusColonIndex == -1) {
                throw new IOException("Invalid JSON: malformed task status");
            }

            int statusStart = taskJson.indexOf("\"", statusColonIndex);

            if (statusStart == -1) {
                throw new IOException("Invalid JSON: malformed task status");
            }

            int statusEnd = taskJson.indexOf("\"", statusStart + 1);

            if (statusEnd == -1) {
                throw new IOException("Invalid JSON: malformed task status");
            }

            TaskStatus status;
            try {
                status = TaskStatus.valueOf(taskJson.substring(statusStart + 1, statusEnd));
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid JSON: unknown task status");
            }
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

    private String unescapeJson(String text) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean skip = false;

        for (int i = 0; i < text.length(); i++) {
            if (skip) {
                skip = false;
                switch (text.charAt(i)) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case '\"':
                        sb.append('\"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    default:
                        throw new IOException("Invalid JSON");
                    // break;
                }
            } else {
                if (text.charAt(i) == '\\') {
                    skip = true;
                    if (i == text.length() - 1) {
                        throw new IOException("Invalid JSON");
                    }
                    continue;
                } else {
                    sb.append(text.charAt(i));
                }
            }
        }
        return sb.toString();
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
