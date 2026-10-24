package com.tasktrackercli;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

        File file = new File("tasks.json.tmp");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(sb.toString());
        }

        Path src = Path.of("tasks.json.tmp");
        Path dest = Path.of("tasks.json");

        try {
            Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            try {
                Files.deleteIfExists(src);
            } catch (IOException e) {
                System.out.println("Warning: Could not delete temporary file: " + e.getMessage());
            }
        }
    }

    public int loadNextId() throws IOException {
        File file = new File("task-metadata.json");
        // StringBuilder sb = new StringBuilder();

        if (!file.exists()) {
            file.createNewFile();
            try (FileWriter writer = new FileWriter(file)) {
                // sb.append("{}");
                writer.write("{}");
            }

            return -1;
        }

        Path filePath = Path.of("task-metadata.json");
        String content = Files.readString(filePath);

        if (content.trim().equals("{}") ||content.trim().isEmpty()) {
            return -1;
        }

        int objectStart = content.indexOf("{");

        int objectEnd = content.indexOf("}", objectStart);

        if (objectEnd == -1) {
            throw new IOException("Invalid JSON: missing closing brace");
        }

        // String object = content.substring(objectStart, objectEnd + 1);

        int nextIdIndex = content.indexOf("\"nextId\"");

        if (nextIdIndex == -1) {
            throw new IOException("Invalid JSON: missing next id");
        }

        int nextIdColonIndex = content.indexOf(":", nextIdIndex);

        if (nextIdColonIndex == -1) {
            throw new IOException("Invalid JSON");
        }

        int nextId;

        try {
            nextId = Integer.parseInt(content.substring(nextIdColonIndex + 1,
                    objectEnd).trim());
        } catch (NumberFormatException e) {
            throw new IOException("Invalid JSON: task id must be an integer");
        }

        if (nextId < 1) {
            throw new IOException("Invalid nextID in JSON");
        }

        return nextId;
    }

    public void saveNextId(int id) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n\"nextId\": ");
        sb.append(String.valueOf(id));
        sb.append("\n}");

        File file = new File("task-metadata.json.temp");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(sb.toString());
        }

        Path src = Path.of("task-metadata.json.temp");
        Path dest = Path.of("task-metadata.json");

        try {
            Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            try {
                Files.deleteIfExists(src);
            } catch (IOException e) {
                System.out.println("Warning: Could not delete temporary file: " + e.getMessage());
            }
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
