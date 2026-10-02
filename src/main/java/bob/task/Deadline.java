package bob.task;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a task with the given description and deadline.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
