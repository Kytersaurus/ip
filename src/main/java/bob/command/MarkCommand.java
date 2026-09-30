package bob.command;

import bob.exception.InvalidTaskNumberError;
import bob.storage.Storage;
import bob.task.Task;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Marks a task as done, saves the list, and displays its updated status.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Creates a command for a zero-based index, validated when executed.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws InvalidTaskNumberError {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberError();
        }
        Task task = tasks.get(index);
        task.markAsDone();
        saveTasks(tasks, ui, storage);
        ui.showTaskStatus(task);
    }
}
