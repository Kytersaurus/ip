package Bob;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/** Saves the current task list to a file on disk. */
public class Storage {
    private static final Path TASK_FILE = Path.of("./data/bob.txt");

    private Storage() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Saves all tasks to the configured task file.
     *
     * @param tasks tasks to save
     * @param taskCount number of occupied entries in {@code tasks}
     * @throws IOException if the directory or file cannot be written
     */
    public static void saveTasks(ArrayList<Task> tasks, int taskCount) throws IOException {
        validateTaskList(tasks, taskCount);
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            lines.add(formatTask(tasks.get(i)));
        }

        try {
            Files.createDirectories(TASK_FILE.getParent());
            Files.write(TASK_FILE, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (SecurityException e) {
            throw new IOException("Task file cannot be written", e);
        }
    }

    /**
     * Loads saved tasks into the supplied task array.
     *
     * @param tasks array that receives the loaded tasks
     * @return number of tasks loaded
     * @throws IOException if the task file cannot be read
     */
    public static int loadTasks(ArrayList<Task> tasks) throws IOException {
        if (tasks == null) {
            throw new IllegalArgumentException("Task array cannot be null");
        }
        try {
            if (!Files.exists(TASK_FILE)) {
                return 0;
            }
            if (!Files.isRegularFile(TASK_FILE)) {
                throw new IOException("Task path is not a regular file");
            }

            int taskCount = 0;
            for (String line : Files.readAllLines(TASK_FILE, StandardCharsets.UTF_8)) {
                Task task = parseTask(line);
                if (task != null && taskCount < tasks.size()) {
                    tasks.add(taskCount++, task);
                }
            }
            return taskCount;
        } catch (SecurityException e) {
            throw new IOException("Task file cannot be read", e);
        }
    }

    private static void validateTaskList(ArrayList<Task> tasks, int taskCount) {
        if (tasks == null) {
            throw new IllegalArgumentException("Task array cannot be null");
        }
        if (taskCount < 0 || taskCount > tasks.size()) {
            throw new IllegalArgumentException("Task count is outside the array bounds");
        }
        for (int i = 0; i < taskCount; i++) {
            if (tasks.get(i) == null) {
                throw new IllegalArgumentException("Task list contains a null task");
            }
        }
    }

    private static Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3 || parts[2].isBlank()
                || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            return null;
        }

        Task task;
        switch (parts[0]) {
        case "D":
            task = parts.length == 4 && !parts[3].isBlank() ? new Deadline(parts[2], parts[3]) : null;
            break;
        case "E":
            task = parts.length == 5 ? new Event(parts[2], parts[3], parts[4]) : null;
            break;
        case "T":
            task = parts.length == 3 ? new Todo(parts[2]) : null;
            break;
        default:
            task = null;
        }

        if (task != null && parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private static String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + task.getDescription() + " | " + deadline.getBy();
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + task.getDescription() + " | "
                    + event.getFrom() + " | " + event.getTo();
        }
        return "T | " + status + " | " + task.getDescription();
    }
}
