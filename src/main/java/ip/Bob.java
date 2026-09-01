package ip;

import java.util.Scanner;

/** Runs the B.O.B. command-line task manager. */
public class Bob {
    private static final int MAX_TASKS = 100;

    /** Starts the B.O.B. command-line application. */
    public static void main(String[] args) {
        String divider = "____________________________________________________________\n";
        String banner = " ____     ___    ____  \n"
                + "| |_) )  / _ \\  | |_) ) \n"
                + "|  _ \\  | | | | |  _ \\ \n"
                + "| |_) | | |_| | | |_) |\n"
                + "|____/   \\___/  |____/ \n";
        System.out.println(divider + banner + "Hello! I'm B.O.B. (Best OpenAI Bot)\n"
                + "What can I do for you?\n" + divider);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String input = in.nextLine().trim();
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println(divider + "Bye. Hope to see you again soon!\n" + divider);
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    System.out.println(divider + "Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                    System.out.println(divider);
                } else if (input.startsWith("mark ")) {
                    int index = Integer.parseInt(input.substring(5)) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsDone();
                        System.out.println(divider + "Ok, I've marked this task as done:\n"
                                + tasks[index] + "\n" + divider);
                    }
                } else if (input.startsWith("unmark ")) {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsNotDone();
                        System.out.println(divider + "Ok, I've marked this task as not done:\n"
                                + tasks[index] + "\n" + divider);
                    }
                } else if (taskCount < MAX_TASKS) {
                    Task task = createTask(input);
                    tasks[taskCount++] = task;
                    System.out.println(divider + "Got it. I've added this task:\n" +
                             task + "\nNow you have " + taskCount
                            + " tasks in the list.\n" + divider);
                }
            }
        }
    }

    private static Task createTask(String input) {
        if (input.startsWith("deadline ")) {
            String[] parts = input.substring(9).split(" /by ", 2);
            return new Deadline(parts[0], parts.length > 1 ? parts[1] : "");
        }
        if (input.startsWith("event ")) {
            String[] parts = input.substring(6).split(" /from ", 2);
            if (parts.length == 2) {
                String[] eventDetails = parts[1].split(" /to ", 2);

                if (eventDetails.length == 2) {
                    return new Event(parts[0], eventDetails[0], eventDetails[1]);
                }

                return new Event(parts[0], eventDetails[0], "");
            }
            return new Event(input.substring(6), "", "");
        }
        if (input.startsWith("todo ")) {
            return new Todo(input.substring(5));
        }
        return new Todo(input);
    }
}
