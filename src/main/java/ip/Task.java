package ip;

public class Task {
    private final String description;
    private final String type;
    private final String dateTime;
    private boolean isDone;

    public Task(String description) {
        this(description, "T", "");
    }

    public Task(String description, String type, String dateTime) {
        this.description = description;
        this.type = type;
        this.dateTime = dateTime;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getDescription() {
        return description;
    }

    public String getTypeIcon() {
        return type;
    }

    public String getDisplayText() {
        if (type.equals("D")) {
            return description + " (by: " + dateTime + ")";
        }
        if (type.equals("E")) {
            return description + " (from: " + dateTime + ")";
        }
        return description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }
}
