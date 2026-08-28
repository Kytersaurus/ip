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
                        System.out.println((i + 1) + "." + "[" + tasks[i].getStatusIcon() + "] "
                                + tasks[i].getDescription());
                    }
                    System.out.println(divider);
                } else if (input.startsWith("mark ")) {
                    int index = Integer.parseInt(input.substring(5)) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsDone();
                    }
                } else if (input.startsWith("unmark ")) {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsNotDone();
                    }
                } else if (taskCount < MAX_TASKS) {
                    tasks[taskCount++] = new Task(input);
                    System.out.println(divider + "added: " + input + '\n' + divider);
                }
            }
        }
    }
}
