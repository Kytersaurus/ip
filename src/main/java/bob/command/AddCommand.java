package bob.command;

import bob.exception.EmptyError;
import bob.exception.SyntaxError;
import bob.parser.Parser;
import bob.storage.Storage;
import bob.task.Task;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Adds a parsed task, saves the updated list, and displays the result.
 */
public class AddCommand extends Command {
    private final String input;

    /**
     * Creates an addition command from input with surrounding whitespace removed.
     * Parsing is deferred so a full list is reported before invalid addition syntax,
     * preserving the application's existing error order.
     *
     * @param input addition command to parse when execution is allowed.
     */
    public AddCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EmptyError, SyntaxError {
        if (tasks.isFull()) {
            ui.showListFull();
            return;
        }
        Task task = Parser.parseTask(input);
        tasks.add(task);
        saveTasks(tasks, ui, storage);
        ui.showTaskAdded(task, tasks.size());
    }
}
