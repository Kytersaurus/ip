package bob.task;

import bob.exception.IndexOutOfBoundsError;

import java.util.ArrayList;
import java.util.List;

/**
 * Owns the tasks in display order and derives their count from the collection.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a list from saved tasks without sharing the supplied collection.
     * Existing files may contain more tasks than the limit for new additions.
     *
     * @param savedTasks tasks loaded from storage, in display order.
     */
    public TaskList(List<Task> savedTasks) {
        tasks = new ArrayList<>(savedTasks);
    }

    /**
     * Returns the current number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list has reached the limit for new additions.
     */
    public boolean isFull() {
        return size() >= MAX_TASKS;
    }

    /**
     * Adds a task at the end of the list.
     *
     * @param task task to add.
     * @throws IllegalStateException if the list is full.
     */
    public void add(Task task) {
        if (isFull()) {
            throw new IllegalStateException("Task list is full");
        }
        tasks.add(task);
    }

    /**
     * Returns the task at the given zero-based index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param index position of the task to remove.
     * @return removed task.
     * @throws IndexOutOfBoundsError if the index is outside the list.
     */
    public Task remove(int index) throws IndexOutOfBoundsError {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsError();
        }
        return tasks.remove(index);
    }
}
