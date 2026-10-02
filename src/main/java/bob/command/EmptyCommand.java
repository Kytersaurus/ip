package bob.command;

import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Ignores blank input without displaying a message or changing tasks.
 */
public class EmptyCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // Blank lines have no effect, matching the existing console behavior.
    }
}
