package Bob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** Runs the B.O.B. command-line task manager. */
public class Bob {
    private static final String DIVIDER = "____________________________________________________________\n";
    private static final int MAX_TASKS = 100;
    private static int taskCount = 0;
    private static final ArrayList<Task> tasks = new ArrayList<>(MAX_TASKS);
    private static final String ITEM_DELETED = "Noted. I've removed this task: ";
    /** Starts the B.O.B. command-line application. */
    public static void main(String[] args) throws EmptyError, SyntaxError {

        String banner = " ____     ___    ____  \n"
                + "| |_) )  / _ \\  | |_) ) \n"
                + "|  _ \\  | | | | |  _ \\ \n"
                + "| |_) | | |_| | | |_) |\n"
                + "|____/   \\___/  |____/ \n";
        System.out.println(DIVIDER + banner + "Hello! I'm B.O.B. (Best OpenAI Bot)\n"
                + "What can I do for you?\n" + DIVIDER);
        tasks.clear();
        taskCount = loadTasks(tasks);
        runCommandLoop();
    }

    private static void runCommandLoop() {
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String input = in.nextLine().trim();

                try {
                    if (input.equalsIgnoreCase("bye")) {
                        System.out.println(DIVIDER + "Bye. Hope to see you again soon!\n" + DIVIDER);
                        break;
                    } else if (input.equalsIgnoreCase("list")) {
                        printTaskList();
                    } else if (input.startsWith("mark ")) {
                        markTask(input);
                    } else if (input.startsWith("unmark ")) {
                        unmarkTask(input);
                    } else if (input.startsWith("delete ")) {
                        deleteTask(input);
                    } else if (!input.isEmpty()) {
                        if (taskCount < MAX_TASKS) {
                            Task task = createTask(input);
                            if (task == null) {
                                throw new SyntaxError();
                            } else {
                                tasks.add(taskCount, task);
                                taskCount++;
                                saveTasks(tasks, taskCount);
                                System.out.println(DIVIDER + "Got it. I've added this task:\n" +
                                        task + "\nNow you have " + taskCount
                                        + " tasks in the list.\n" + DIVIDER);
                            }
                        } else {
                        System.out.println(DIVIDER + "Your task list is full.\n" + DIVIDER);
                        }
                    }
                } catch (EmptyError e) {
                    System.out.println(e.getErrorMessage());
                } catch (SyntaxError e) {
                    System.out.println(e.getErrorMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid task number.");
                } catch (IndexOutOfBoundsError e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static int loadTasks(ArrayList<Task> tasks) {
        try {
            return Storage.loadTasks(tasks);
        } catch (IOException e) {
            System.out.println("I couldn't load your saved tasks.");
            return 0;
        }
    }

    private static void parseInput() {

    }

    private static void printTaskList() {
        System.out.println(DIVIDER + "Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    private static void markTask(String input) {
        int index = parseTaskIndex(input.substring(5), taskCount);
        tasks.get(index).markAsDone();
        saveTasks(tasks, taskCount);
        System.out.println(DIVIDER + "Ok, I've marked this task as done:\n"
                + tasks.get(index) + "\n" + DIVIDER);
    }

    private static void unmarkTask(String input) {
        int index = parseTaskIndex(input.substring(7), taskCount);
        tasks.get(index).markAsNotDone();
        saveTasks(tasks, taskCount);
        System.out.println(DIVIDER + "Ok, I've marked this task as not done:\n"
                + tasks.get(index) + "\n" + DIVIDER);
    }
    private static int parseTaskIndex(String input, int taskCount) {
        int index = Integer.parseInt(input.trim()) - 1;
        if (index < 0 || index >= taskCount) {
            throw new NumberFormatException("Task number is outside the list");
        }
        return index;
    }

    private static void saveTasks(ArrayList<Task> tasks, int taskCount) {
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
        } else {
            throw new SyntaxError();
        }
    }

    private static void deleteTask(String input) throws IndexOutOfBoundsError {
        int index = Integer.parseInt(input.substring(7).trim()) - 1;
        if (index < 0 || index >= taskCount) {
            throw new IndexOutOfBoundsError();
        }
        Task deletedTask = tasks.remove(index);
        taskCount--;
        saveTasks(tasks, taskCount);
        System.out.println(DIVIDER + ITEM_DELETED + "\n  " + deletedTask + "\nNow you have "
                + taskCount + " tasks in the list\n" + DIVIDER);
    }
}
