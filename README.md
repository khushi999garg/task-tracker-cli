# Task Tracker CLI

A command-line Task Tracker application built using **Java 21** and **Maven**. It allows users to manage tasks directly from the terminal and stores task data locally in JSON files.

## Features

- Add new tasks
- List all tasks
- Filter tasks by status
- Update task titles
- Mark tasks as in progress or done
- Delete tasks
- Automatically generate unique task IDs
- Store task creation and update timestamps
- Persist tasks between application runs
- Validate command-line arguments and user input
- Handle malformed JSON data

## Tech Stack

- **Language:** Java 21
- **Build Tool:** Maven
- **Storage:** JSON files
- **Testing Framework:** JUnit 4

## Project Structure

```text
task-tracker-cli/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/tasktrackercli/
│   │           ├── App.java
│   │           ├── Task.java
│   │           ├── TaskService.java
│   │           ├── TaskRepository.java
│   │           └── TaskStatus.java
│   │
│   └── test/
│       └── java/
│           └── com/tasktrackercli/
│               └── AppTest.java
│
├── tasks.json
├── task-metadata.json
├── pom.xml
└── README.md
```

### Architecture

- **App:** Handles command-line arguments and user interaction.
- **Task:** Represents a task and its properties.
- **TaskService:** Contains task management and business logic.
- **TaskRepository:** Handles JSON file storage and retrieval.
- **TaskStatus:** Defines the available task statuses.

## Requirements

Make sure you have the following installed:

- Java JDK 21 or later
- Apache Maven

Verify your installation:

```bash
java -version
mvn -version
```

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/khushi999garg/task-tracker-cli.git
```

### 2. Navigate to the project directory

```bash
cd task-tracker-cli
```

### 3. Build the project

```bash
mvn clean compile
```

### 4. Run the application

The application currently runs through the Java classpath:

```bash
java -cp target/classes com.tasktrackercli.App
```

## Available Commands

### Add a task

```bash
java -cp target/classes com.tasktrackercli.App add "Learn Java"
```

### List all tasks

```bash
java -cp target/classes com.tasktrackercli.App list
```

### List tasks by status

```bash
java -cp target/classes com.tasktrackercli.App list TODO
```

```bash
java -cp target/classes com.tasktrackercli.App list IN_PROGRESS
```

```bash
java -cp target/classes com.tasktrackercli.App list DONE
```

### List tasks that are not done

```bash
java -cp target/classes com.tasktrackercli.App list NOT_DONE
```

### Mark a task as in progress

```bash
java -cp target/classes com.tasktrackercli.App mark-in-progress 1
```

### Mark a task as done

```bash
java -cp target/classes com.tasktrackercli.App mark-done 1
```

### Update a task

```bash
java -cp target/classes com.tasktrackercli.App update 1 "Learn Spring Boot"
```

### Delete a task

```bash
java -cp target/classes com.tasktrackercli.App delete 1
```

## Data Storage

The application uses two JSON files in the current working directory:

- **tasks.json:** Stores task details, statuses, and timestamps.
- **task-metadata.json:** Stores metadata used to maintain unique task IDs across application runs.

The files are created automatically when required.

## Task Properties

Each task contains:

| Property | Description |
|---|---|
| `id` | Unique task identifier |
| `title` | Task title |
| `status` | TODO, IN_PROGRESS, or DONE |
| `createdAt` | Task creation timestamp |
| `updatedAt` | Last modification timestamp |

## Future Improvements

- Add comprehensive automated unit tests.
- Improve CLI output and user experience.
- Add better handling of concurrent file access.
- Explore using a JSON library for serialization and parsing.

## Project Reference

This project is based on the **Task Tracker CLI** challenge from roadmap.sh.

[roadmap.sh – Task Tracker Project](https://roadmap.sh/projects/task-tracker)

## Author

Khushi Garg
