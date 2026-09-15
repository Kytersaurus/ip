package Bob;

import java.io.IOException;
import java.util.Scanner;

/** Runs the B.O.B. command-line task manager. */
public class Bob {
    private static final int MAX_TASKS = 100;

    /** Starts the B.O.B. command-line application. */
    public static void main(String[] args) throws EmptyError, SyntaxError {
        String divider = "____________________________________________________________\n";
        String banner = " ____     ___    ____  \n"
                + "| |_) )  / _ \\  | |_) ) \n"
                + "|  _ \\  | | | | |  _ \\ \n"
                + "| |_) | | |_| | | |_) |\n"
                + "|____/   \\___/  |____/ \n";
        System.out.println(divider + banner + "Hello! I'm B.O.B. (Best OpenAI Bot)\n"
                + "What can I do for you?\n" + divider);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = loadTasks(tasks);
        runCommandLoop(tasks, taskCount, divider);
    }

    private static void runCommandLoop(Task[] tasks, int taskCount, String divider)
            throws SyntaxError, EmptyError {
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String input = in.nextLine().trim();

                try {
                    if (input.equalsIgnoreCase("bye")) {
                        System.out.println(divider + "Bye. Hope to see you again soon!\n" + divider);
                        break;
                    } else if (input.equalsIgnoreCase("list")) {
                        printTaskList(tasks, taskCount, divider);
                    } else if (input.startsWith("mark ")) {
                        markTask(input, tasks, taskCount, divider);
                    } else if (input.startsWith("unmark ")) {
                        unmarkTask(input, tasks, taskCount, divider);
                    } else if (!input.isEmpty()) {
                        if (taskCount < MAX_TASKS) {
                            Task task = createTask(input);
                            if (task == null) {
                                throw new SyntaxError();
                            } else {
                                tasks[taskCount++] = task;
                                saveTasks(tasks, taskCount);
                                System.out.println(divider + "Got it. I've added this task:\n" +
                                        task + "\nNow you have " + taskCount
                                        + " tasks in the list.\n" + divider);
                            }
                        } else {
                            System.out.println(divider + "Your task list is full.\n" + divider);
                        }
                    }
                } catch (EmptyError e) {
                    System.out.println(e.getErrorMessage());
                } catch (SyntaxError e) {
                    System.out.println(e.getErrorMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid task number.");
                }
            }
        }
    }

    private static int loadTasks(Task[] tasks) {
        try {
            return Storage.loadTasks(tasks);
        } catch (IOException e) {
            System.out.println("I couldn't load your saved tasks.");
            return 0;
        }
    }

    private static void parseInput() {

    }

    private static void printTaskList(Task[] tasks, int taskCount, String divider) {
        System.out.println(divider + "Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println(divider);
    }

    private static void markTask(String input, Task[] tasks, int taskCount, String divider) {
        int index = parseTaskIndex(input.substring(5), taskCount);
        tasks[index].markAsDone();
        saveTasks(tasks, taskCount);
        System.out.println(divider + "Ok, I've marked this task as done:\n"
                + tasks[index] + "\n" + divider);
    }

    private static void unmarkTask(String input, Task[] tasks, int taskCount, String divider) {
        int index = parseTaskIndex(input.substring(7), taskCount);
        tasks[index].markAsNotDone();
        saveTasks(tasks, taskCount);
        System.out.println(divider + "Ok, I've marked this task as not done:\n"
                + tasks[index] + "\n" + divider);
    }

    private static int parseTaskIndex(String input, int taskCount) {
        int index = Integer.parseInt(input.trim()) - 1;
        if (index < 0 || index >= taskCount) {
            throw new NumberFormatException("Task number is outside the list");
        }
        return index;
    }

    private static void saveTasks(Task[] tasks, int taskCount) {
        try {
            Storage.saveTasks(tasks, taskCount);
        } catch (IOException e) {
            System.out.println("I couldn't save your tasks to disk.");
        }
    }

    private static Task createTask(String input) throws SyntaxError, EmptyError {
        if (input.startsWith("deadline ")) {
            String[] parts = input.substring(9).split(" /by ", 2);
            if (parts[0].trim().isEmpty() || parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new SyntaxError();
            }
            return new Deadline(parts[0].trim(), parts[1].trim());
        }
        if (input.startsWith("event ")) {
            String eventInput = input.substring(6).trim();
            if (eventInput.startsWith("/from") || eventInput.startsWith("/to")) {
                throw new SyntaxError();
            }
            String[] parts = eventInput.split(" /from ", 2);
            if (parts.length == 2) {
                String[] eventDetails = parts[1].split(" /to ", 2);

                if (eventDetails.length == 2) {
                    if (parts[0].trim().isEmpty() || eventDetails[0].trim().isEmpty()
                            || eventDetails[1].trim().isEmpty()) {
                        throw new SyntaxError();
                    }
                    return new Event(parts[0].trim(), eventDetails[0].trim(), eventDetails[1].trim());
                }

                if (parts[0].trim().isEmpty() || eventDetails[0].trim().isEmpty()) {
                    throw new SyntaxError();
                }
                return new Event(parts[0].trim(), eventDetails[0].trim(), "");
            }
            if (eventInput.isEmpty()) {
                throw new EmptyError();
            }
            return new Event(eventInput, "", "");
        }
        if (input.equals("todo") || input.startsWith("todo ")) {
            String task = input.length() > 4 ? input.substring(4).trim() : "";
                if (task.isEmpty()) {
                    throw new EmptyError();
                } else {
                    return new Todo(task);
                }
        }
        else {
            throw new SyntaxError();
        }
    }
}
