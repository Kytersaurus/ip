package ip;

/** Represents a task with a description and completion status. */
public class Task {
    private final String description;
    private boolean isDone;

    /** Creates a task with the given description. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns the status icon for this task. */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns this task's description. */
    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }
}
