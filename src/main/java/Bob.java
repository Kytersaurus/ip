import java.util.Scanner;

public class Bob {
    public static void main(String[] args) {
        String banner = " ____     ___    ____  \n"
                + "| |_) )  / _ \\  | |_) ) \n"
                + "|  _ \\  | | | | |  _ \\ \n"
                + "| |_) | | |_| | | |_) |\n"
                + "|____/   \\___/  |____/ \n";

        String intro = "Hello! I'm B.O.B. (Best OpenAI Bot)\n";
        String start = "What can I do for you?\n";
        String exit = "Bye. Hope to see you again soon!\n";
        String divider = "____________________________________________________________\n";
        System.out.println(divider + banner + intro + start + divider);
        Task[] tasks = new Task[100];
        int taskCount = 0;
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                String input = in.nextLine();
                if (input.trim().equalsIgnoreCase("bye")) {
                    System.out.println(divider + exit + divider);
                    break;
                }
                else if (input.trim().equalsIgnoreCase("list")) {
                    System.out.println(divider + "Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + "[" + tasks[i].getStatusIcon() + "] " + tasks[i].getDescription());
                    }
                    System.out.println(divider);
                }
                else if (input.trim().startsWith("mark ")) {
                    int index = Integer.parseInt(input.trim().substring(5)) -1;
                    if (index >= taskCount) {
                        System.out.println(divider + "No task at specified index " + (index + 1) + " found\n" + divider);
                        continue;
                    }
                    tasks[index].markAsDone();
                    System.out.println(divider + "Ok, I've marked this task as done:\n" + "[" + tasks[index].getStatusIcon() + "] " + tasks[index].getDescription() + "\n" + divider);
                }
                else if (input.trim().startsWith("unmark ")) {
                    int index = Integer.parseInt(input.trim().substring(7)) -1;
                    if (index >= taskCount) {
                        System.out.println(divider + "No task at specified index " + (index + 1) + " found\n" + divider);
                        continue;
                    }
                    tasks[index].markAsNotDone();
                    System.out.println(divider + "Ok, I've marked this task as not done:\n" + "[" + tasks[index].getStatusIcon() + "] " + tasks[index].getDescription() + "\n" + divider);
                }
                else {
                    System.out.println(divider + "added: " + input.trim() + '\n' + divider);
                    Task newTask = new Task(input.trim());
                    taskCount++;
                    tasks[taskCount-1] = newTask;
                }
            }
        }
    }
}
